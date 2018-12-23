package itcast.com.itcastsafe.activity.engine

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import itcast.com.itcastsafe.R
import itcast.com.itcastsafe.activity.bean.TaskInfo
import java.lang.Exception

/**
 *@Author:MingKong
 *@Description:
 *@Date:Created in 20:03 2018/12/22
 *@Modified By:
 */
object TaskInfoParaser {

     fun getTaskInfos(context:Context):List<TaskInfo>{

         var taskInfos:MutableList<TaskInfo> = arrayListOf();
         //获取进程管理者
         val am: ActivityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager;
         for(runingAppService in am.runningAppProcesses){
             var taskInfo = TaskInfo();
             //获取进程的名称
              taskInfo.packageName=runingAppService.processName;
             try {
                 //获取内存信息
                 val processMemoryInfo = am.getProcessMemoryInfo(intArrayOf(runingAppService.pid))
                 //Dirty弄脏
                 //获取总共弄脏多少内存（当前应用占用多少内存）
                 taskInfo.memorySize=(processMemoryInfo[0].totalPrivateDirty*1024).toLong()

                 val packageInfo = context.packageManager.getPackageInfo(runingAppService.processName, 0)
                 //获取应用图标
                 taskInfo.icon=packageInfo.applicationInfo.loadIcon(context.packageManager)

                 //获取应用名称
                 taskInfo.appName=packageInfo.applicationInfo.loadLabel(context.packageManager).toString()

                 val flag = packageInfo.applicationInfo.flags
                 taskInfo.isUserApp = flag and ApplicationInfo.FLAG_SYSTEM == 0

             }catch (e:Exception){
                 taskInfo.appName=runingAppService.processName
                 taskInfo.icon=context.getResources().getDrawable(
                         R.mipmap.ic_launcher_default);

             }

             taskInfos.add(taskInfo)


         }

         return taskInfos
     }

}