package es.udc.psi.p25barbeito;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import es.udc.psi.p25barbeito.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Emisiones propias
    public static final String ACTION_GENERAL = "es.udc.PSI.broadcast.GENERAL";
    public static final String ACTION_LOCAL_END = "es.udc.PSI.broadcast.LOCAL_END";
    public static final String ACTION_BOUND_END = "es.udc.PSI.broadcast.BOUND_END";
    // Parámetros de los servicios y la tarea: cuenta y tiempo de espera
    public static final String KEY_COUNT = "KEY_COUNT";
    public static final String KEY_TIME = "KEY_TIME";
    public static final int DEFAULT_COUNT = 10;
    public static final int DEFAULT_TIME = 500; // 0,5 segundos

    String TAG = "_TAG";

    ActivityMainBinding binding;

    BroadcastReceiver br = new MyBroadcastReceiver();

    MyBoundService myBoundService;
    boolean mBound = false;

    CountTask myCountTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Receptor registrado en el contexto (diapositiva 6)
        IntentFilter filter = new IntentFilter(Intent.ACTION_USER_PRESENT);
        filter.addAction(ACTION_GENERAL);
        filter.addAction(ACTION_LOCAL_END);
        filter.addAction(ACTION_BOUND_END);
        ContextCompat.registerReceiver(this, br, filter, ContextCompat.RECEIVER_EXPORTED);

        setUI();
    }

    private void setUI() {
        // 1. Botón para lanzar la emisión propia (diapositiva 8)
        binding.btnBroadcast.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(ACTION_GENERAL);
            intent.putExtra("data", "Mi propio tipo de notificación");
            sendBroadcast(intent);
        });

        // 3. Switch_1: lanzar (y parar) el servicio local (diapositivas 11 y 13)
        binding.swLocalService.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d(TAG, "swLocalService: " + isChecked);
            Intent service = new Intent(getApplicationContext(), MyLocalService.class);
            if (isChecked) {
                service.putExtra(KEY_COUNT, getCount());
                service.putExtra(KEY_TIME, DEFAULT_TIME);
                startService(service);
            } else {
                stopService(service);
            }
        });

        // 5. Switch_2: lanzar (y parar) el servicio enlazado (diapositiva 23)
        binding.swBindService.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d(TAG, "swBindService: " + isChecked);
            if (isChecked) {
                Intent intent = new Intent(getApplicationContext(), MyBoundService.class);
                intent.putExtra(KEY_COUNT, getCount());
                intent.putExtra(KEY_TIME, DEFAULT_TIME);
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
            } else if (mBound) {
                unbindService(connection);
                mBound = false;
            }
        });

        // Botón Get (diapositiva 22): comprobar siempre que hay una conexión válida
        binding.btnGet.setOnClickListener(v -> {
            if (mBound) {
                int num = myBoundService.getCount();
                binding.tvText.setText("Bound count: " + num);
            }
        });

        // Botón Set: modificar el iterador con el valor del texto editable
        binding.btnSet.setOnClickListener(v -> {
            if (mBound) {
                myBoundService.setCount(getCount());
            }
        });

        // 6. Switch_3: tarea en segundo plano (diapositivas 35 y 36)
        binding.swBackgroundTask.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d(TAG, "swBackgroundTask: " + isChecked);
            if (isChecked) {
                myCountTask = new CountTask();
                myCountTask.execute(getCount(), DEFAULT_TIME);
            } else if (myCountTask != null) {
                myCountTask.cancel(true);
            }
        });
    }

    // Valor del EditText (cuenta)
    private int getCount() {
        try {
            return Integer.parseInt(binding.etCount.getText().toString());
        } catch (NumberFormatException e) {
            return DEFAULT_COUNT;
        }
    }

    /** Defines callbacks for service binding, passed to bindService() (diapositiva 24) */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to MyBoundService, cast the IBinder and get the instance
            MyBoundService.MyBinder binder = (MyBoundService.MyBinder) service;
            myBoundService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    // Receptor de emisiones (diapositiva 5). Declarado en la actividad: tiene acceso a sus variables
    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String log = "Action: " + intent.getAction() + "\n";
            log = log + "URI: " + intent.toUri(Intent.URI_INTENT_SCHEME) + "\n";
            Toast.makeText(context, log, Toast.LENGTH_SHORT).show();
            Log.d(TAG, log);

            // 4. y 5.d. Al finalizar su cuenta, el servicio pone su switch a off
            if (ACTION_LOCAL_END.equals(intent.getAction())) {
                binding.swLocalService.setChecked(false);
            } else if (ACTION_BOUND_END.equals(intent.getAction())) {
                binding.swBindService.setChecked(false);
            }
        }
    }

    // Tarea en segundo plano (diapositiva 35): entrada, progreso y salida
    public class CountTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            int count = params[0];
            int time = params[1];
            for (int i = 0; i < count; i++) {
                Log.d(TAG, "CountTask: " + i);
                publishProgress(i);
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                // Escape early if cancel() is called
                if (isCancelled()) break;
            }
            return "Done";
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "CountTask init ...");
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            binding.tvText.setText("Tarea 2º plano: " + progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            binding.tvText.setText("Tarea 2º plano: " + result);
            binding.swBackgroundTask.setChecked(false);
        }

        @Override
        protected void onCancelled() {
            Log.d(TAG, "CountTask cancelled");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Muy importante: anular el registro del receptor (diapositiva 6)
        unregisterReceiver(br);
        if (mBound) {
            unbindService(connection);
            mBound = false;
        }
        if (myCountTask != null) {
            myCountTask.cancel(true);
        }
    }
}
