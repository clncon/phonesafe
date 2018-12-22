package itcast.com.itcastsafe.activity.bean

import android.graphics.drawable.Drawable

/**
 *@Author:MingKong
 *@Description:
 *@Date:Created in 19:58 2018/12/22
 *@Modified By:
 */
class TaskInfo {
  lateinit var icon:Drawable
  lateinit var packageName:String
  lateinit var appName:String
   var memorySize:Long=0

    /**
     * 是否是用户进程
     */
    var  isUserApp:Boolean=false

}