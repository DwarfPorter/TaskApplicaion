package ru.geekbrains.taskapplication;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textHello;
    private EditText editMillisecs;
    private TextView textCount;
    int counterClick = 0;

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

    private void executeAsyncTask(){
        new WaitAsyncTask().execute(getMillisecs());
    }

    private int getMillisecs(){
        return Integer.parseInt(editMillisecs.getText().toString());
    }

    private void calculate(int millisec) {
        try {
            Thread.sleep(getMillisecs());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

}
