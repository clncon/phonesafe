package itcast.com.itcastsafe.activity.utils;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**
 * @Author:MingKong
 * @Description:
 * @Date:Created in 19:54 2018/11/13
 * @Modified By:
 */
public class ServiceStatusUtils {


    public static  boolean isServiceRunning(Context ctx , String serviceName){
       ActivityManager am  = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);
         for(ActivityManager.RunningServiceInfo info:runningServices){
             if(serviceName.equals(info.service.getClassName())){
                 return true;
             }
         }
        return false;

    }
}
