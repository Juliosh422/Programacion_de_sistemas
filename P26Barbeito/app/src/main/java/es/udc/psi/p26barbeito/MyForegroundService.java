package es.udc.psi.p26barbeito;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.ServiceCompat;

public class MyForegroundService extends Service {

    private static final String TAG = "P26_FGS";

    public static final String EXTRA_MAX = "max";
    public static final String ACTION_STOP = "es.udc.psi.p26barbeito.ACTION_STOP";
    private static final int DEFAULT_MAX = 10;
    private static final int FGS_NOTIFICATION_ID = 2;

    private Thread counterThread;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 6d: al pulsar la notificación llega esta acción -> parar hilo y servicio
        if (intent != null && ACTION_STOP.equals(intent.getAction())) {
            Log.d(TAG, "Parada solicitada desde la notificación");
            stopCounter();
            stopForegroundAndSelf();
            return START_NOT_STICKY;
        }

        // startForeground es obligatorio tras startForegroundService (también en un 2º arranque)
        MainActivity.createNotificationChannel(this);
        int type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                ? ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC : 0;
        ServiceCompat.startForeground(this, FGS_NOTIFICATION_ID, buildNotification(), type);

        // 5a: sólo un servicio/hilo activo
        if (isRunning()) {
            Log.d(TAG, "El servicio ya está en ejecución: no se inicia otro");
            return START_NOT_STICKY;
        }

        // 6c: volver a comprobar los permisos antes de iniciar el hilo
        if (!MainActivity.hasNotificationPermission(this)) {
            Log.w(TAG, "Falta el permiso POST_NOTIFICATIONS: se para el servicio");
            stopForegroundAndSelf();
            return START_NOT_STICKY;
        }

        int max = intent != null ? intent.getIntExtra(EXTRA_MAX, DEFAULT_MAX) : DEFAULT_MAX;
        startCounter(max);
        return START_NOT_STICKY;
    }

    private boolean isRunning() {
        return counterThread != null && counterThread.isAlive();
    }

    // 6a/6b: conteo ascendente, interrumpible, en un hilo, con log y sleep de 1000 ms
    private void startCounter(int max) {
        counterThread = new Thread(() -> {
            try {
                for (int i = 1; i <= max && !Thread.currentThread().isInterrupted(); i++) {
                    Log.d(TAG, "Cuenta: " + i + "/" + max);
                    Thread.sleep(1000);
                }
                Log.d(TAG, "Conteo terminado");
            } catch (InterruptedException e) {
                Log.d(TAG, "Conteo interrumpido");
            } finally {
                stopForegroundAndSelf();
            }
        }, "CounterThread");
        counterThread.start();
    }

    private void stopCounter() {
        if (counterThread != null) {
            counterThread.interrupt();
            counterThread = null;
        }
    }

    private void stopForegroundAndSelf() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE);
        stopSelf();
    }

    private Notification buildNotification() {
        Intent stopIntent = new Intent(this, MyForegroundService.class);
        stopIntent.setAction(ACTION_STOP);
        PendingIntent stopPendingIntent = PendingIntent.getService(this, 0, stopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.outline_fgs_24)
                .setContentTitle("Foreground service P26")
                .setContentText("Contando... pulsa para parar")
                .setContentIntent(stopPendingIntent)
                .setOngoing(true)
                .build();
    }

    @Override
    public void onDestroy() {
        stopCounter();
        Log.d(TAG, "Servicio destruido");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
