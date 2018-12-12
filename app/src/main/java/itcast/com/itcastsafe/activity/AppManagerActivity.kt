package itcast.com.itcastsafe.activity

import android.app.Activity
import android.os.*
import android.text.format.Formatter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import itcast.com.itcastsafe.R
import itcast.com.itcastsafe.activity.adapter.MyBaseAdapter
import itcast.com.itcastsafe.activity.bean.AppInfo
import itcast.com.itcastsafe.activity.engine.AppInfos
import kotlinx.android.synthetic.main.activity_app_manager.*

class AppManagerActivity : Activity() {

    private  var appInfos:List<AppInfo>?=null
    private var adapter:AppManagerAdapter?=null ;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_manager)
        initUI()
        initData()
    }

    private fun initUI() {
        //获取rom内存运行的剩余空间大小
        val freeSpace = Environment.getDataDirectory().freeSpace

        //获取sd卡的剩余空间
        val sd_freeSpace = Environment.getExternalStorageDirectory().freeSpace

        Formatter.formatFileSize(this,freeSpace)
        tv_memory.text= "运行内存：${Formatter.formatFileSize(this,freeSpace)}"
        tv_sd.text= "sd卡可用：${Formatter.formatFileSize(this,sd_freeSpace)}"


    }

    val handler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            var listView = lisv_view as ListView
            if(listView.adapter==null){
                adapter = AppManagerAdapter()
                listView.adapter=adapter
            }else{
                 adapter!!.notifyDataSetChanged()
            }


        }
    }

    class ViewHolder(var viewItem:View){
        val iv_icon: ImageView = viewItem.findViewById(R.id.iv_icon)
        val tv_name: TextView = viewItem.findViewById(R.id.tv_name)
        val tv_size: TextView = viewItem.findViewById(R.id.tv_size)
        val tv_type: TextView = viewItem.findViewById(R.id.tv_type)
    }

    inner class AppManagerAdapter : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
           var view:View?=null
            var viewHolder:ViewHolder?=null
            if(convertView==null){
                view = View.inflate(this@AppManagerActivity,R.layout.item_app_manager,null)
                viewHolder= ViewHolder(view)
                view.tag=viewHolder
            }else{
                view=convertView
                viewHolder= view.tag as ViewHolder


            }

            val info:AppInfo = getItem(position) as AppInfo
            viewHolder.iv_icon.background=info.icon
            viewHolder.tv_name.text=info.apkPackageName
            viewHolder.tv_size.text=Formatter.formatFileSize(this@AppManagerActivity, info.apkSize!!)
            if(info.isRom!!){
                viewHolder.tv_type.text="手机内存"
            }else{
                viewHolder.tv_type.text="SD卡内存"

            }


            return view!!
        }

        override fun getItem(position: Int): Any {
            return appInfos!!.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
           return appInfos!!.size
        }

    }


    private fun initData(){

        Thread(){
           kotlin.run {
               //get all app that is installed
                appInfos = AppInfos.getAppInfos(this@AppManagerActivity)
               handler.sendEmptyMessage(0)

           }
        }.start()
    }


}
