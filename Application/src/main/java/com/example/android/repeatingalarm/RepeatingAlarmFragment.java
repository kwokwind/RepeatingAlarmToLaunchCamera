/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.repeatingalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.common.logger.*;

public class RepeatingAlarmFragment extends Fragment {
    public static final String TAG = "RepeatingAlarmFragment";
    private boolean isRunning = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.sample_action) {
            Log.d(TAG, "isRunning is " + isRunning);
            if (!isRunning) {
                Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("action_for_loop", true);
                final PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager alarmManager =
                        (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + 2000L, pendingIntent);

                item.setTitle("Stop Loop");
                isRunning = true;

                Log.i("RepeatingAlarmFragment", "Loop is started...");
            } else {
                Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("action_for_loop", false);
                final PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager alarmManager =
                        (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + 2000L, pendingIntent);

                item.setTitle("Start Loop");
                isRunning = false;

                Log.i("RepeatingAlarmFragment", "Loop is stopped...");
            }
        }
        return true;
    }
}