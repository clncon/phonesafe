package itcast.com.itcastsafe.activity.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Debug

/**
 * @Author:MingKong
 * @Description:
 * @Date:Created in 19:21 2018/12/22
 * @Modified By:
 */
object SystemInfoUtils {

    /**
     * service is running
     */
    fun isServiceRunnint(context: Context, className: String): Boolean {
        val am:ActivityManager  = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager;
        val infos =am.getRunningServices(200);
         for(info in infos){
             val serviceClassName  = info.service.className
             if(className==serviceClassName){
                 return true
             }
         }

        return false
    }


    /**
     *  get process count
     */
    fun getProcessCount(context: Context):Int{
        //获取进程管理者
        val am:ActivityManager  = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager;
        //获取当前手机上面所有运行的进程
         return  am.runningAppProcesses.size


    }

    /**
     * get aviable memory
     */
    fun getAvaiMem(context:Context):Long{
        val am:ActivityManager  = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager;
        var memoryInfo = ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo)
        return memoryInfo.availMem

    }

    /**
     * get total meory
     */
    fun getTotalMem(context:Context):Long{
        val am:ActivityManager  = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager;
        var memoryInfo = ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo)
        return memoryInfo.totalMem

    }




}
