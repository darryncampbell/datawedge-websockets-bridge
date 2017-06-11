package com.darryncampbell.datawedge_websockets_bridge;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobServiceToStartServerFromBoot extends JobService {

    private static final String TAG = "Datawedge WS Bridge";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "Job started to initiate WebSocket IntentService");
        //  Something strange is afoot with the context and our WebSocket Server.  I have found it
        //  most reliable if we reschedule the job here before starting our WebSocket Server
        //  reschedule the job if it emanated from the boot receiver
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler tm = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
            tm.cancelAll();
            if (params.getJobId() == BootReceiver.JOB_ID)
            {
                Log.i(TAG, "Rescheduling websocket Job");
                ComponentName jobService = new ComponentName(getApplicationContext(), JobServiceToStartServerFromBoot.class);
                JobInfo.Builder builder = new JobInfo.Builder(BootReceiver.JOB_ID + 1, jobService);
                builder.setOverrideDeadline(2000);
                builder.setMinimumLatency(1000);
                builder.setRequiresCharging(false);
                builder.setRequiresDeviceIdle(false);
                builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
                tm.schedule(builder.build());
            }
            else
            {
                //  We should have the right context now
                Intent startIntent = new Intent(getApplicationContext(), WebSocketIntentService.class);
                getApplicationContext().startService(startIntent);
            }

        }

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "Job stopped which called WebSocket IntentService on boot");
        return true;
    }
}
