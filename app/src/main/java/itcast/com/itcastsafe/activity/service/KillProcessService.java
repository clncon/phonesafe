package itcast.com.itcastsafe.activity.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class KillProcessService extends Service {
    LockScreenReceiver lockScreenReceiver;
    public KillProcessService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class LockScreenReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            ActivityManager am  = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
            System.out.println("---------------------start kill process-------------------------");
            for(ActivityManager.RunningAppProcessInfo processInfo : runningAppProcesses){
                am.killBackgroundProcesses(processInfo.processName);
            }

        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        lockScreenReceiver = new LockScreenReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(lockScreenReceiver,filter);
        Timer timer = new Timer();
         TimerTask tt = new TimerTask(){

             @Override
             public void run() {

             }
         };
         timer.schedule(tt,1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(lockScreenReceiver);
    }
}
