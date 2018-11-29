package ru.geekbrains.taskapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

public class WaitService extends Service {

    private final IBinder binder = new WaitBinder();

    public WaitService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void asyncMessage(final int millisecs, final MainActivity.OnServiceResultListener resultListener){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                calculate(millisecs);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        resultListener.onServiceResult("Hello from service!");
                    }
                });
            }
        }).start();
    }

    private void calculate(int millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class WaitBinder extends Binder {
        public WaitService getService(){
            return WaitService.this;
        }
    }
}
