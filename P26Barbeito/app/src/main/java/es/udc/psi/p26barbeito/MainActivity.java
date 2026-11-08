package es.udc.psi.p26barbeito;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;

import es.udc.psi.p26barbeito.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "P26_Main";

    public static final String CHANNEL_ID = "p26_channel";
    private static final int NOTIFICATION_ID = 1;
    private static final String KEY_BUTTON = "button_pressed";

    private ActivityMainBinding binding;

    // Lista dinámica + adapter (atributos para poder borrar / deshacer)
    private ArrayList<String> dataList;
    private ArrayAdapter<String> adapter;

    // requestCode distinto en cada PendingIntent para que no se pisen los extras
    private int requestCode = 0;

    // Acción pendiente de ejecutar cuando el usuario conceda el permiso
    private Runnable pendingAction;

    private final ActivityResultLauncher<String> notificationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) {
                    Log.d(TAG, "Permiso POST_NOTIFICATIONS concedido");
                    if (pendingAction != null) pendingAction.run();
                } else {
                    Toast.makeText(this, "Sin permiso de notificaciones no se puede continuar",
                            Toast.LENGTH_LONG).show();
                }
                pendingAction = null;
            });

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

        createNotificationChannel(this);
        setupListView();
        showPressedButton(getIntent());
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        showPressedButton(intent);
    }

    // ---------------------------------------------------------------------
    // Menú de opciones
    // ---------------------------------------------------------------------

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Por defecto el menú extendido (⋮) no muestra iconos: forzarlos
        if (menu instanceof MenuBuilder) {
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            runWithNotificationPermission(this::showNotification);
            return true;
        } else if (id == R.id.action_snack) {
            showSnackbar();
            return true;
        } else if (id == R.id.action_clear) {
            NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
            return true;
        } else if (id == R.id.action_msg) {
            Toast.makeText(this, "Mensaje desde el menú (Msg)", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_fgs) {
            runWithNotificationPermission(this::startFgs);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ---------------------------------------------------------------------
    // Snackbar
    // ---------------------------------------------------------------------

    private void showSnackbar() {
        Snackbar.make(binding.getRoot(), "Esto es un Snackbar", Snackbar.LENGTH_LONG)
                .setAction("Log", v -> Log.d(TAG, "Botón del Snackbar pulsado"))
                .show();
    }

    // ---------------------------------------------------------------------
    // Notificación con 3 botones
    // ---------------------------------------------------------------------

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    context.getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            context.getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }

    public static boolean hasNotificationPermission(Context context) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
                || ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED;
    }

    /** Ejecuta la acción si hay permiso; si no, lo pide y la ejecuta al concederse. */
    private void runWithNotificationPermission(Runnable action) {
        if (hasNotificationPermission(this)) {
            action.run();
        } else {
            pendingAction = action;
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
    }

    private PendingIntent buttonPendingIntent(String buttonName) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(KEY_BUTTON, buttonName);
        return PendingIntent.getActivity(this, requestCode++, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_add_24)
                .setContentTitle("Notificación P26")
                .setContentText("Pulsa uno de los botones")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .addAction(R.drawable.outline_msg_24, "Botón 1", buttonPendingIntent("Botón 1"))
                .addAction(R.drawable.outline_msg_24, "Botón 2", buttonPendingIntent("Botón 2"))
                .addAction(R.drawable.outline_msg_24, "Botón 3", buttonPendingIntent("Botón 3"));

        // Comprobación explícita (lint la exige antes de notify)
        if (hasNotificationPermission(this)) {
            NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build());
        }
    }

    private void showPressedButton(Intent intent) {
        if (intent == null) return;
        String button = intent.getStringExtra(KEY_BUTTON);
        if (button != null) {
            binding.tvButton.setText("Pulsado: " + button);
            NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
        }
    }

    // ---------------------------------------------------------------------
    // Foreground service
    // ---------------------------------------------------------------------

    private void startFgs() {
        Intent intent = new Intent(this, MyForegroundService.class);
        intent.putExtra(MyForegroundService.EXTRA_MAX, 10);
        ContextCompat.startForegroundService(this, intent);
    }

    // ---------------------------------------------------------------------
    // ListView + menú contextual
    // ---------------------------------------------------------------------

    private void setupListView() {
        String[] data = {"zero", "one", "two", "three", "four", "five", "six"}; // 7 elementos
        dataList = new ArrayList<>(Arrays.asList(data));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        binding.listView.setAdapter(adapter);
        registerForContextMenu(binding.listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (info == null) return super.onContextItemSelected(item);
        int position = info.position;

        int id = item.getItemId();
        if (id == R.id.ctx_delete) {
            Toast.makeText(this, "Delete: índice " + position, Toast.LENGTH_SHORT).show();
            deleteItem(position);
            return true;
        } else if (id == R.id.ctx_share) {
            Toast.makeText(this, "Share: índice " + position, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void deleteItem(int position) {
        String removed = dataList.remove(position);
        adapter.notifyDataSetChanged();

        Snackbar.make(binding.getRoot(), "Borrado: " + removed, Snackbar.LENGTH_LONG)
                .setAction("Undo", v -> {
                    dataList.add(Math.min(position, dataList.size()), removed);
                    adapter.notifyDataSetChanged();
                })
                .show();
    }
}
