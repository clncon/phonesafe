package itcast.com.itcastsafe.activity

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.format.Formatter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import itcast.com.itcastsafe.R
import itcast.com.itcastsafe.activity.bean.AppInfo
import itcast.com.itcastsafe.activity.bean.TaskInfo
import itcast.com.itcastsafe.activity.engine.TaskInfoParaser
import itcast.com.itcastsafe.activity.utils.SystemInfoUtils
import kotlinx.android.synthetic.main.activity_task_manager.*
import kotlinx.android.synthetic.main.item_process_manager.view.*

class TaskManagerActivity : Activity() {
   lateinit var taskInfos:List<TaskInfo>
    lateinit var systemInfos:List<TaskInfo>
    lateinit var userInfos:List<TaskInfo>
    lateinit var adapter:TaskInfoAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_manager)
        initUI();
        initData();
    }

    /**
     * 加载数据
     */
    fun initUI(){

         var avaiMem = SystemInfoUtils.getAvaiMem(this@TaskManagerActivity)
         var totalMem = SystemInfoUtils.getTotalMem(this@TaskManagerActivity)
         tv_process_count.text="当前进程数：${SystemInfoUtils.getProcessCount(this@TaskManagerActivity).toString()} 个";
         tv_process_memory.text="剩余/总共：${Formatter.formatFileSize(this@TaskManagerActivity,avaiMem)}/${Formatter.formatFileSize(this@TaskManagerActivity,totalMem)}"

    }


    /**
     * 初始化数据
     */
    fun initData(){

         Thread(){

             kotlin.run {
                    taskInfos =TaskInfoParaser.getTaskInfos(this@TaskManagerActivity)
                    userInfos = taskInfos!!.groupBy { it.isUserApp }.getValue(true)
                    systemInfos = taskInfos!!.groupBy { it.isUserApp }.getValue(false)
                    this@TaskManagerActivity.runOnUiThread {
                        var listView = list_view as ListView
                        if(listView.adapter==null){
                            adapter = TaskInfoAdapter()
                            listView.adapter=adapter
                        }else{
                            adapter.notifyDataSetChanged()
                        }

                    }

             }
         }.start()
    }


   inner class TaskInfoAdapter: BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            //如果当前position==0标识应用程序
            if(position==0){
                var textView = TextView(this@TaskManagerActivity)
                textView.text="用戶进程（${userInfos.size}）"
                textView.setTextColor(Color.WHITE)
                textView.setBackgroundColor(Color.GRAY)
                return textView

            }else if(position==userInfos.size+1){
                var textView = TextView(this@TaskManagerActivity)
                textView.text="系統进程（${systemInfos.size}）"
                textView.setTextColor(Color.WHITE)
                textView.setBackgroundColor(Color.GRAY)
                return textView


            }
            var info: TaskInfo?=null
            if(position<userInfos.size+1){
                //把多出來的特殊條目剪掉
                info = userInfos.get(position-1)
            }else {
                info=systemInfos.get(position-(userInfos.size+2))

            }
            lateinit var view:View
            lateinit var viewHolder:ViewHolder
            if(convertView!=null&&convertView is LinearLayout){
                view=convertView
                viewHolder= view.tag as TaskManagerActivity.ViewHolder
            }else {
                view = View.inflate(this@TaskManagerActivity,R.layout.item_process_manager,null)
                viewHolder= TaskManagerActivity.ViewHolder(view)
                view.tag=viewHolder
            }

            viewHolder.iv_app_icon.background=info.icon
            viewHolder.tv_app_name.text=info.appName
            viewHolder.tv_app_merory_size.text=Formatter.formatFileSize(this@TaskManagerActivity,info.memorySize)

            return view

        }

        override fun getItem(position: Int): Any? {
            if(position==0){
                return null
            }else if(position==userInfos.size+1){
                return null
            }
            var taskInfo:TaskInfo?=null

            if(position<userInfos.size+1){
                //把多出来的特殊条目减去
                taskInfo = userInfos.get(position-1)
            }else {
                taskInfo = systemInfos.get(position-userInfos.size-2)
            }
            return taskInfo
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return userInfos.size+systemInfos.size+2
        }

    }


    class ViewHolder(var viewItem:View){

        val iv_app_icon: ImageView = viewItem.findViewById(R.id.iv_app_icon)
        val tv_app_name: TextView = viewItem.findViewById(R.id.tv_app_name)
        val tv_app_merory_size: TextView = viewItem.findViewById(R.id.tv_app_merory_size)
        val cb_app_status: CheckBox = viewItem.findViewById(R.id.cb_app_status)
    }

}
