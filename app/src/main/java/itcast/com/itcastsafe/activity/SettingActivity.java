package itcast.com.itcastsafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import itcast.com.itcastsafe.activity.view.SettingItemClick;
import itcast.com.itcastsafe.activity.view.SettingItemView;

public class SettingActivity extends Activity {
    private SharedPreferences sharedPreferences;
    private SettingItemView settingItemView;
    private SettingItemView addresstemView;
    private SettingItemClick svAddressStyle;
    private SettingItemClick sv_address_style;
    final String[] items = new String[]{"半透明","活力橙","卫视蓝","金属灰","苹果绿"};
    private SettingItemClick sv_address_loaction;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settingItemView = findViewById(R.id.sv_update);
        addresstemView = findViewById(R.id.sv_address);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        sv_update();
        sv_address();
        initAddressStyle();
        initAddressLocation();


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

    /**
     * 修改提示框登录风格
     */
    public void initAddressStyle(){

        sv_address_style = findViewById(R.id.sv_address_style);

        sv_address_style.setTv_title("修改提示框归属地风格");
        int adressStyle = sharedPreferences.getInt("AdressStyle", 0);

        sv_address_style.setTv_desc(items[adressStyle]);
    }

    /**
     * 初始化归属地位置View
     */
    public void initAddressLocation(){
        sv_address_loaction = findViewById(R.id.sv_address_location);
        sv_address_loaction.setTv_title("归属地提示框显示位置");
        sv_address_loaction.setTv_desc("设置归属地提示框的显示位置");
        sv_address_loaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this,DragViewActivity.class));
            }
        });
    }



    /**
     * 弹出显示风格的单选框
     */
    public void updateStyle(View view){
        final int adressStyle = sharedPreferences.getInt("AdressStyle", 0);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("归属地提示风格");
        builder.setSingleChoiceItems(items, adressStyle, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPreferences.edit().putInt("AdressStyle",which).commit();
                sv_address_style.setTv_desc(items[which]);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消",null);

        builder.show();
    }

}
