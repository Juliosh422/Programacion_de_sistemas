package es.udc.psi.p25barbeito;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyLocalService extends Service {
    String TAG = "_TAG";

    Thread countThread;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        String log = " MyLocalService ha sido ejecutado";
        Log.d(TAG, log);
        Toast.makeText(getApplicationContext(), log, Toast.LENGTH_SHORT).show();

        // Parámetros: cuenta y tiempo de espera
        int count = MainActivity.DEFAULT_COUNT;
        int time = MainActivity.DEFAULT_TIME;
        if (intent != null) {
            count = intent.getIntExtra(MainActivity.KEY_COUNT, MainActivity.DEFAULT_COUNT);
            time = intent.getIntExtra(MainActivity.KEY_TIME, MainActivity.DEFAULT_TIME);
        }

        // Tarea pesada: en un hilo secundario (diapositivas 15 y 38)
        final int finalCount = count;
        final int finalTime = time;
        Runnable countRun = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < finalCount; i++) {
                    Log.d(TAG, "MyLocalService: " + i);
                    try {
                        Thread.sleep(finalTime);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                // Al finalizar su cuenta: emisión propia (diapositiva 8) y parada (stopSelf)
                Intent end = new Intent();
                end.setAction(MainActivity.ACTION_LOCAL_END);
                end.putExtra("data", "MyLocalService ha terminado su cuenta");
                sendBroadcast(end);
                stopSelf();
            }
        };
        countThread = new Thread(countRun);
        countThread.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countThread != null) {
            countThread.interrupt();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
