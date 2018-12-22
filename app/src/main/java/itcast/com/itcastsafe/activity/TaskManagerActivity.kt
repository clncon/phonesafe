package itcast.com.itcastsafe.activity

import android.app.Activity
import android.os.Bundle
import android.text.format.Formatter
import android.view.Menu
import android.view.MenuItem
import itcast.com.itcastsafe.R
import itcast.com.itcastsafe.activity.utils.SystemInfoUtils
import kotlinx.android.synthetic.main.activity_task_manager.*

class TaskManagerActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_manager)
        initUI();
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

}
