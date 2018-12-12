package itcast.com.itcastsafe.activity

import android.app.Activity
import android.os.Bundle
import android.os.Environment
import android.text.format.Formatter
import android.view.Menu
import android.view.MenuItem
import itcast.com.itcastsafe.R
import kotlinx.android.synthetic.main.activity_app_manager.*

class AppManagerActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_manager)
        initUI()
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


}
