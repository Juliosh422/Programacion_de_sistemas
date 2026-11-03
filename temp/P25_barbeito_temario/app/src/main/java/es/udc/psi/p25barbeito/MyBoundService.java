package es.udc.psi.p25barbeito;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyBoundService extends Service {
    String TAG = "_TAG";

    // Iterador de la cuenta (descendente)
    private volatile int count = MainActivity.DEFAULT_COUNT;
    private int time = MainActivity.DEFAULT_TIME;
    private Thread countThread;

    /** methods for clients */
    public int getCount() {
        return count;
    }

    public void setCount(int newCount) {
        count = newCount;
    }

    // Binder given to clients
    private final IBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        MyBoundService getService() {
            // Return this instance of MyBoundService so clients can call public methods
            return MyBoundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Parámetros: cuenta y tiempo de espera
        count = intent.getIntExtra(MainActivity.KEY_COUNT, MainActivity.DEFAULT_COUNT);
        time = intent.getIntExtra(MainActivity.KEY_TIME, MainActivity.DEFAULT_TIME);

        // Tarea pesada: en un hilo secundario (diapositivas 15 y 38)
        Runnable countRun = new Runnable() {
            @Override
            public void run() {
                while (count > 0) {
                    Log.d(TAG, "MyBoundService: " + count);
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        return;
                    }
                    count--;
                }
                // Al finalizar su cuenta: emisión propia (diapositiva 8)
                Intent end = new Intent();
                end.setAction(MainActivity.ACTION_BOUND_END);
                end.putExtra("data", "MyBoundService ha terminado su cuenta");
                sendBroadcast(end);
            }
        };
        countThread = new Thread(countRun);
        countThread.start();

        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countThread != null) {
            countThread.interrupt();
        }
    }
}
