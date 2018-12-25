package itcast.com.itcastsafe.activity

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.format.Formatter
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.safframework.log.L
import itcast.com.itcastsafe.R
import itcast.com.itcastsafe.activity.bean.AppInfo
import itcast.com.itcastsafe.activity.bean.TaskInfo
import itcast.com.itcastsafe.activity.engine.TaskInfoParaser
import itcast.com.itcastsafe.activity.utils.SystemInfoUtils
import itcast.com.itcastsafe.activity.utils.ToastUtil
import itcast.com.itcastsafe.activity.utils.UIUtils
import kotlinx.android.synthetic.main.activity_task_manager.*
import kotlinx.android.synthetic.main.item_process_manager.view.*

class TaskManagerActivity : Activity() {
   lateinit var taskInfos:List<TaskInfo>
    lateinit var systemInfos:MutableList<TaskInfo>
    lateinit var userInfos:MutableList<TaskInfo>
    lateinit var adapter:TaskInfoAdapter
     var avaiMem:Long=0
     var totalMem:Long=0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_manager)
        initUI();
        initData();
        bt_select_all.setOnClickListener{v ->
            //ToastUtil.showToast(this@TaskManagerActivity,"ff")
            for(taskInfo in userInfos){
                taskInfo.checked=true

            }

            for(taskInfo in systemInfos){
                taskInfo.checked=true
            }

            adapter.notifyDataSetChanged()

        }
    }

    /**
     * 加载数据
     */
    fun initUI(){

          avaiMem= SystemInfoUtils.getAvaiMem(this@TaskManagerActivity)
          totalMem= SystemInfoUtils.getTotalMem(this@TaskManagerActivity)
         tv_process_count.text="当前进程数：${SystemInfoUtils.getProcessCount(this@TaskManagerActivity).toString()} 个";
         tv_process_memory.text="剩余/总共：${Formatter.formatFileSize(this@TaskManagerActivity,avaiMem)}/${Formatter.formatFileSize(this@TaskManagerActivity,totalMem)}"

        var listView = list_view as ListView
        listView.setOnItemClickListener { parent, view, position, id ->
            //获取点击对象
            var obj = listView.getItemAtPosition(position);
            var viewHolder:ViewHolder  = view.tag as ViewHolder

            if (obj != null && obj is TaskInfo) {
                var taskInfo: TaskInfo = obj as TaskInfo
                taskInfo.checked = !taskInfo.checked
               viewHolder.cb_app_status.isChecked=taskInfo.checked

            }

        }


    }


    /**
     * 初始化数据
     */
    fun initData(){

         Thread(){

             kotlin.run {
                    taskInfos =TaskInfoParaser.getTaskInfos(this@TaskManagerActivity)
                    userInfos = taskInfos!!.groupBy { it.isUserApp }.getValue(true).toMutableList()
                    systemInfos = taskInfos!!.groupBy { it.isUserApp }.getValue(false).toMutableList()
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

    /**
     * 反选
     */
    fun selectOppsite(v:View){

         for(taskInfo in userInfos){
           taskInfo.checked=!taskInfo.checked
         }

        for(taskInfo in systemInfos){
            taskInfo.checked=!taskInfo.checked
        }
        adapter.notifyDataSetChanged()
    }

    /**
     * 清理进程
     */
    fun killProcess(v:View){

        //清理的进程的个数
         var totalCount=0;
        //释放内存的大小
        var releaseMem:Long =0;
      //获取进程管理器
        val activityManager:ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val removeList = mutableListOf<TaskInfo>()
        for(taskInfo in userInfos){
            if(taskInfo.checked){
                removeList.add(taskInfo)
                totalCount++
                releaseMem+=taskInfo.memorySize;
                avaiMem+=taskInfo.memorySize
            }
        }
        for(taskInfo in systemInfos){
            if(taskInfo.checked){
                removeList.add(taskInfo)
                totalCount++
                releaseMem+=taskInfo.memorySize
                avaiMem+=taskInfo.memorySize
            }
        }

        //杀死进程参数的包名
        for(taskInfo in removeList){
            if(taskInfo.isUserApp){
                userInfos.remove(taskInfo)
                activityManager.killBackgroundProcesses(taskInfo.packageName)
            }else{
                systemInfos.remove(taskInfo)
                activityManager.killBackgroundProcesses(taskInfo.packageName)
            }

        }

        tv_process_count.text="当前进程数：${SystemInfoUtils.getProcessCount(this@TaskManagerActivity).toString()} 个";
        tv_process_memory.text="剩余/总共：${Formatter.formatFileSize(this@TaskManagerActivity,avaiMem)}/${Formatter.formatFileSize(this@TaskManagerActivity,totalMem)}"
        UIUtils.showToast(this@TaskManagerActivity,"共清理了${totalCount},释放了${Formatter.formatFileSize(this@TaskManagerActivity,releaseMem)}")
        //刷新界面
        adapter.notifyDataSetChanged()

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
            viewHolder.cb_app_status.isChecked=info.checked


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
