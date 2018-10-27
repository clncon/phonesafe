package itcast.com.itcastsafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import itcast.com.itcastsafe.R;
import itcast.com.itcastsafe.activity.view.SettingItemView;

public class SettingActivity extends Activity {
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        final SettingItemView settingItemView = findViewById(R.id.sv_update);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        settingItemView.setTv_title("自动更新设置");
        if(sharedPreferences.getBoolean("auto_update",true)){

            settingItemView.setTv_desc("自动更新设置已经开启");
            settingItemView.setIsChecked(true);
        }else{

            settingItemView.setTv_desc("自动更新设置已经关闭");
            settingItemView.setIsChecked(false);
        }

        settingItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   //判断是否选中
                   if(settingItemView.isChecked()){
                        settingItemView.setIsChecked(false);
                        settingItemView.setTv_desc("自动更新设置已经关闭");
                        sharedPreferences.edit().putBoolean("auto_update",false).commit();
                   }else{
                       settingItemView.setIsChecked(true);
                       settingItemView.setTv_desc("自动更新设置已经开启");
                       sharedPreferences.edit().putBoolean("auto_update",true).commit();
                   }
            }
        });


    }


   

}
