package ru.geekbrains.taskapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.work.Data;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    private TextView textHello;
    private EditText editMillisecs;
    private TextView textCount;
    int counterClick = 0;

//region service
    WaitService waitService;
    private boolean isBound = false;

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = new Intent(this, WaitService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound){
            unbindService(connection);
            isBound = false;
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            WaitService.WaitBinder binder = (WaitService.WaitBinder) service;
            waitService = binder.getService();
            isBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    public interface OnServiceResultListener{
        void onServiceResult(String string);
    }

    private void executeService(){
        if (isBound){
            waitService.asyncMessage(getMillisecs(), new OnServiceResultListener() {
                @Override
                public void onServiceResult(String string) {
                    textHello.setText(string);
                }
            });
        }
    }
//endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textHello = findViewById(R.id.textHello);
        textCount = findViewById(R.id.textCount);
        editMillisecs = findViewById(R.id.editMillisecs);

        Button clickCount = findViewById(R.id.buttonClick);
        clickCount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                counterClick++;
                textCount.setText(Integer.toString(counterClick));
            }
        });

        Button buttonSimple = findViewById(R.id.buttonSimple);
        buttonSimple.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                executeSimple();
            }
        });

        Button buttonThread = findViewById(R.id.buttonThread);
        buttonThread.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                executeThread();
            }
        });

        Button buttonAsynctask = findViewById(R.id.buttonAsynctask);
        buttonAsynctask.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                executeAsyncTask();
            }
        });

        Button buttonService = findViewById(R.id.buttonService);
        buttonService.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                executeService();
            }
        });
    }

    private void executeSimple(){
        calculate(getMillisecs());
        textHello.setText("Hello from activity!");
    }

    private void executeThread(){
        final Handler handler = new Handler();
        final int millisecs = getMillisecs();
        new Thread(new Runnable() {
            @Override
            public void run() {
                calculate(millisecs);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        textHello.setText("Hello from thread!");
                    }
                });
            }
        }).start();
    }

    private void executeWorker(){
        Data input = new Data.Builder().putInt("MilliSeconds", getMillisecs()).build();
        WorkManager.getInstance().beginWith()
    }

//region delay operation
    private int getMillisecs(){
        return Integer.parseInt(editMillisecs.getText().toString());
    }

    private void calculate(int millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
//endregion

//region AsyncTask
    private void executeAsyncTask(){
        new WaitAsyncTask().execute(getMillisecs());
    }

    private class WaitAsyncTask extends AsyncTask<Integer, Void, String>{

        @Override
        protected String doInBackground(Integer... integers) {
            calculate(integers[0]);
            return "Hello from AsyncTask!";
        }

        @Override
        protected void onPostExecute(String s) {
            textHello.setText(s);
        }
    }
//endregion
}
