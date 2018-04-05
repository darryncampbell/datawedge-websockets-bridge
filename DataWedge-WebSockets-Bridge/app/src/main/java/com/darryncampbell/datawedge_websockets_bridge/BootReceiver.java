package com.darryncampbell.datawedge_websockets_bridge;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "Datawedge WS Bridge";
    public static final int JOB_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        //  Start the WebSockets Bridge activity (note: starting the WebSocketIntentService directly
        //  does not work as the context is invalid after this method exits)
        Log.i(TAG, "Boot receiver");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Log.i(TAG, "Starting DataWedge WebSocket Bridge after boot (L+)");
            ComponentName jobService = new ComponentName(context, JobServiceToStartServerFromBoot.class);
            JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, jobService);
            builder.setOverrideDeadline(2000);
            builder.setMinimumLatency(1000);
            builder.setRequiresCharging(false);
            builder.setRequiresDeviceIdle(false);
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            JobScheduler tm = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
            tm.schedule(builder.build());
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            //  Testing with KK, the context issues with the Websocket class I was seeing under M
            //  whilst testing last year do not appear to exist so we can start the service directly.
            Log.i(TAG, "Starting DataWedge WebSocket Bridge after boot (KK)");
            Intent startIntent = new Intent(context, WebSocketIntentService.class);
            context.startService(startIntent);
        }
    }
}
