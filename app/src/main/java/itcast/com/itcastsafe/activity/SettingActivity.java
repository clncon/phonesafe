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
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        final SettingItemView settingItemView = findViewById(R.id.sv_update);
        settingItemView.setTv_title("自动更新设置");
        settingItemView.setTv_desc("自动更新设置已经关闭");
        SharedPreferences sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);

        settingItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   //判断是否选中
                   if(settingItemView.isChecked()){
                        settingItemView.setIsChecked(false);
                        settingItemView.setTv_desc("自动更新设置已经关闭");
                   }else{
                       settingItemView.setIsChecked(true);
                       settingItemView.setTv_desc("自动更新设置已经开启");
                   }
            }
        });


    }


   

}
