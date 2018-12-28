package itcast.com.itcastsafe.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.EditText
import com.safframework.log.L
import itcast.com.itcastsafe.R
import itcast.com.itcastsafe.activity.service.KillProcessService
import itcast.com.itcastsafe.activity.utils.SystemInfoUtils
import kotlinx.android.synthetic.main.activity_task_manager_setting.*

class TaskManagerSettingActivity : Activity() {

    lateinit var sharedPreferences:SharedPreferences;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_manager_setting)
        sharedPreferences = getSharedPreferences("config", Context.MODE_PRIVATE)
        initUI()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.task_manager_setting, menu)
        return true
    }

    fun initUI(){

        var flag = sharedPreferences.getBoolean("is_show_system",false)
         cb_kill_status.isChecked=flag
         cb_kill_status.setOnCheckedChangeListener{ compoundButton: CompoundButton, isChecked: Boolean ->

              L.i("------>:"+isChecked.toString())
             if(isChecked){
                 sharedPreferences.edit().putBoolean("is_show_system",true).commit()
             }else{
                 sharedPreferences.edit().putBoolean("is_show_system",false).commit()
             }
         }


        val intent = Intent()
        intent.setClass(this@TaskManagerSettingActivity,KillProcessService().javaClass)
        cb_status_kill.setOnCheckedChangeListener{ compoundButton: CompoundButton, flag: Boolean ->


            if(flag){
                startService(intent)
            }else{
                stopService(intent)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        cb_status_kill.isChecked = SystemInfoUtils.isServiceRunnint(this@TaskManagerSettingActivity,
                "itcast.com.itcastsafe.activity.service.KillProcessService")


    }
}
