package itcast.com.itcastsafe.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import itcast.com.itcastsafe.R;
import itcast.com.itcastsafe.activity.view.SettingItemView;

public class Setup2Activity extends BaseSetupActivity{


    private SettingItemView sv_bind;
    private static String[] PERMISSIONS_PHONE_STATE = {
            Manifest.permission.READ_PHONE_STATE};
    private static int REQUEST_PERMISSION_CODE = 0;
    SharedPreferences mConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        sv_bind = findViewById(R.id.sv_bind);
        mConfig = getSharedPreferences("config", MODE_PRIVATE);
        String sim = mConfig.getString("sim",null);
        if(!TextUtils.isEmpty(sim)){
            sv_bind.setIsChecked(true);
        }else{
            sv_bind.setIsChecked(false);

        }

        sv_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sv_bind.isChecked()){
                    sv_bind.setIsChecked(false);
                    mConfig.edit().remove("sim").commit();
                }else {
                    sv_bind.setIsChecked(true);
                    //获取sim卡信息
                    TelephonyManager telephonyManager  = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String simSerialNumber = telephonyManager.getSimSerialNumber();//获取sim卡序列号
                    //System.out.println("sim序列号："+simSerialNumber);
                    mConfig.edit().putString("sim",simSerialNumber).commit();
                };
            }
        });

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_PHONE_STATE, REQUEST_PERMISSION_CODE);
            }
        }
    }


    /*
     * 申请读取手机状态的权限
     * */
    @Override
    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }


    public void showPreviousPage(){
        startActivity(new Intent(Setup2Activity.this,Setup1Activity.class));
        finish();
        /**
         * 两个界面切换的动画
         */
        overridePendingTransition(R.anim.previous_in,R.anim.previous_out);

    }

    public void showNextPage(){
        startActivity(new Intent(Setup2Activity.this,Setup3Activity.class));
        finish();
        /**
         * 两个界面切换的动画
         */
        overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setup2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
