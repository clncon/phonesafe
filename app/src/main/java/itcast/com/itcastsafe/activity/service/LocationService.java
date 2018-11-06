package itcast.com.itcastsafe.activity.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class LocationService extends Service {
    SharedPreferences mConfig;
    MyLocationListener listener;
    private LocationManager lm;

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mConfig = getSharedPreferences("config", MODE_PRIVATE);
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);//是否允许付费，比如使用3g网络
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = lm.getBestProvider(criteria, true);//获取最佳位置提供者
         listener = new MyLocationListener();
         lm.requestLocationUpdates(bestProvider,0,0,listener);

    }

    class MyLocationListener implements LocationListener{


        //位置发生变化时
        @Override
        public void onLocationChanged(Location location) {
            System.out.println("get location!");
            mConfig.edit().putString("location","j:"+location.getLongitude()+";w:"+location.getLongitude()).commit();
            stopSelf();//停止service
        }

        //位置提供者状态发生了变化
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            System.out.println("onStatusChanged");
        }

        //用户打开gps
        @Override
        public void onProviderEnabled(String provider) {
            System.out.println("onProviderEnabled");
        }

        //用户关闭gps
        @Override
        public void onProviderDisabled(String provider) {
            System.out.println("onProviderDisabled");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lm.removeUpdates(listener);//当acitvity销毁时，停止更新位置，节省电量
    }
}
