package itcast.com.itcastsafe.activity.bean

import android.graphics.drawable.Drawable

/**
 *@Author:MingKong
 *@Description:
 *@Date:Created in 20:46 2018/12/12
 *@Modified By:
 * @param icon 图标
 * @param apkSize 大小
 * @param userApp 是用户app（true）还是系统app(false)
 */
class AppInfo {

    var icon:Drawable?=null
    var apkSize:Long?=null
    var userApp:Boolean?=null
    var apkPackageName:String?=null
    var isRom:Boolean?=null
    var apkName:String?=null
}