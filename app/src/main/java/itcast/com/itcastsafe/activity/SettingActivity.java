package itcast.com.itcastsafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import itcast.com.itcastsafe.R;
import itcast.com.itcastsafe.activity.service.AddressService;
import itcast.com.itcastsafe.activity.utils.ServiceStatusUtils;
import itcast.com.itcastsafe.activity.view.SettingItemView;

public class SettingActivity extends Activity {
    private SharedPreferences sharedPreferences;
    private SettingItemView settingItemView;
    private SettingItemView addresstemView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settingItemView = findViewById(R.id.sv_update);
        addresstemView = findViewById(R.id.sv_address);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        sv_update();
        sv_address();


    }

    public void sv_address(){
        if(ServiceStatusUtils.isServiceRunning(this,"itcast.com.itcastsafe.activity.service.AddressService")){
            addresstemView.setIsChecked(true);
        }else{
            addresstemView.setIsChecked(false);

        }

        addresstemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addresstemView.isChecked()){
                    stopService(new Intent(SettingActivity.this,AddressService.class));
                    addresstemView.setIsChecked(false);

                }else{
                    startService(new Intent(SettingActivity.this, AddressService.class));
                    addresstemView.setIsChecked(true);
                }
            }
        });
    }
    public void sv_update(){
        // settingItemView.setTv_title("自动更新设置");
        if(sharedPreferences.getBoolean("auto_update",true)){

            //  settingItemView.setTv_desc("自动更新设置已经开启");
            settingItemView.setIsChecked(true);
        }else{

            //settingItemView.setTv_desc("自动更新设置已经关闭");
            settingItemView.setIsChecked(false);
        }

        settingItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否选中
                if(settingItemView.isChecked()){
                    settingItemView.setIsChecked(false);
                    //settingItemView.setTv_desc("自动更新设置已经关闭");
                    sharedPreferences.edit().putBoolean("auto_update",false).commit();
                }else{
                    settingItemView.setIsChecked(true);
                    //settingItemView.setTv_desc("自动更新设置已经开启");
                    sharedPreferences.edit().putBoolean("auto_update",true).commit();
                }
            }
        });
    }
   

}
