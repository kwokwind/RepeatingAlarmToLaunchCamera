package com.example.android.repeatingalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;

import com.example.android.common.logger.Log;

/**
 * Created by rtf743 on 6/19/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static final String TAG = "AlarmReceiver";
    public static final long MIN_DELAY_SEC = 10;
    public static final long DEFAULT_DELAY_SEC = 80;
    public static long SLEEP_SEC_MS = DEFAULT_DELAY_SEC*1000L;
    private boolean mKeepRunning = false;

    public void openCameraThread(final Context context)
    {
        Log.i(TAG, "openCameraThread enter");
        // Create Inner Thread Class
        Thread background = new Thread(new Runnable() {
            // After call for background.start this run method call
            public void run() {
                try {
                    openCamera(context);
                } catch (Throwable t) {
                    // just end the background thread
                    Log.i(TAG, "Thread  exception " + t);
                }
            }
        });
        // Start Thread
        background.start();  //After call start method thread called run Method
    }

    public void openCamera(Context context)
    {
        Log.i(TAG, "openCamera enter");
        Intent takePictureIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE);
        takePictureIntent.setPackage("com.motorola.camera2");
        takePictureIntent.putExtra("motorola.intent.extra.CALLER", "quick-draw");
        //takePictureIntent.putExtra(Intent.EXTRA_KEY_EVENT, event);
        takePictureIntent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        context.startActivity(takePictureIntent);
        SystemClock.sleep(4000);
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startMain);
        Log.i(TAG, "openCamera quit");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
/*
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"/system/bin/getprop",
                    "persist.mytest.delay_sec"});
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            if (reader != null && reader.readLine().length() > 0) {
                long tmp = Long.parseLong(reader.readLine()); // To DO, there is always crash here
                if (tmp < MIN_DELAY_SEC) {
                    tmp = MIN_DELAY_SEC;
                }
                SLEEP_SEC_MS = tmp * 1000L;
            }
        } catch (IOException e) {
            Log.e(TAG, "getprop failed with exception " + e);
        }
*/
        Bundle bundle = intent.getExtras();
        mKeepRunning = bundle.getBoolean("action_for_loop");
        Log.i(TAG, "onReceive intent! context is " + context.getClass().toString()
                + ", delay is " + SLEEP_SEC_MS/1000 + " seconds, mKeepRunning is " + mKeepRunning);

        if (mKeepRunning) {
            openCameraThread(context);

            Intent newIntent = new Intent(context, this.getClass());
            final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, newIntent, 0);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                    + SLEEP_SEC_MS, pendingIntent);
        } else {
            Log.d(TAG, "stop running!");
        }
    }
}