package itcast.com.itcastsafe.activity.engine

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import itcast.com.itcastsafe.activity.bean.AppInfo
import java.io.File

/**
 *@Author:MingKong
 *@Description:
 *@Date:Created in 20:45 2018/12/12
 *@Modified By:
 */
object AppInfos {

        fun getAppInfos(context: Context):List<AppInfo>{
            val installedApplications= context.packageManager.getInstalledApplications(0)
             val appInfos:MutableList<AppInfo> = arrayListOf();
             for(info in installedApplications){

                 val appInfo = AppInfo()
                 val icon = info.loadIcon(context.packageManager)
                 val apkName = info.loadLabel(context.packageManager)
                 val apkSize = File(info.sourceDir).length()

                 print("---------------------------------------------------------")
                 print("icon:${icon}")
                 print("apkName:${apkName}")
                 print("apkSize:${apkSize}")

                 appInfo.icon=icon
                 appInfo.apkName=apkName.toString()
                 appInfo.apkPackageName=info.packageName
                 appInfo.apkSize=apkSize
                 //获取到安装应用程序的标记
                 val flags = info.flags

                 appInfo.userApp = (flags and ApplicationInfo.FLAG_SYSTEM) == 0
                 appInfo.isRom = (flags and ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0

                 appInfos.add(appInfo)

             }

            return appInfos

        }
}