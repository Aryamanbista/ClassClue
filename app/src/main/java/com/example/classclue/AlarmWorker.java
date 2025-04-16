package com.example.classclue;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class AlarmWorker extends Worker {
    public static final String KEY_EVENT_TITLE = "eventTitle";

    public AlarmWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Retrieve the event title from the input data
        String eventTitle = getInputData().getString(KEY_EVENT_TITLE);
        if (eventTitle != null) {
            // Implement your alarm logic here (e.g., show a notification)
            // For now, just log the event title
            System.out.println("Alarm for event: " + eventTitle);
        }
        return Result.success();
    }
}