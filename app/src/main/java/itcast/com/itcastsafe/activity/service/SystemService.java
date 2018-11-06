package itcast.com.itcastsafe.activity.service;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import itcast.com.itcastsafe.activity.receiver.AdminReceiver;
import itcast.com.itcastsafe.activity.utils.ToastUtil;

public class SystemService extends Service {

    private DevicePolicyManager mDPMx;
    ComponentName mDeviceAdminSample;
    private String type;

    public SystemService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        type = intent.getStringExtra("type");
        return null;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        
        mDPMx = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDeviceAdminSample = new ComponentName(this, AdminReceiver.class);
        SharedPreferences mConfig = getSharedPreferences("config", MODE_PRIVATE);
        String type = mConfig.getString("type",null);
        if("wipedata".equals(type)){
            clearData();
        }else if("lockscreen".equals(type)){
           lockScreen();
        }

        stopSelf();
    }

    //激活设备管理器，也可以在设置-》安全-》设备管理器中手动激活
    public void activeAdmin(){
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,mDeviceAdminSample);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"请激活设备管理器");
        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
        
        startActivity(intent);
    }


    //一键锁屏
     public void lockScreen(){
      if(mDPMx.isAdminActive(mDeviceAdminSample)){//判断设备管理器是否已经激活
          mDPMx.lockNow();
          mDPMx.resetPassword("123456",0);
      }else{
          activeAdmin();
          ToastUtil.showToast(this,"必须先激活设备管理器");

      }
     }

    public void clearData() {
        if (mDPMx.isAdminActive(mDeviceAdminSample)) {// 判断设备管理器是否已经激活
            mDPMx.wipeData(0);// 清除数据,恢复出厂设置
        } else {
            activeAdmin();
            ToastUtil.showToast(this,"必须先激活设备管理器");
        }
    }
}
