package ru.geekbrains.taskapplication;


import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WaitWorker extends Worker implements ConstantWorker {
    public WaitWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        int millisecs = getInputData().getInt(MilliSeconds, 0);
        calculate(millisecs);
        Data output = new Data.Builder().putString(StringResult, "Hello from worker!").build();
        setOutputData(output);
        return Result.SUCCESS;
    }

    private void calculate(int millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
