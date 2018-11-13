package itcast.com.itcastsafe.activity.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import itcast.com.itcastsafe.activity.dao.AddressDao;

public class AddressService extends Service {

    private TelephonyManager tm;

    public AddressService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        MyListener myListener = new MyListener();
        tm.listen(myListener,PhoneStateListener.LISTEN_CALL_STATE);//监听来电状态
    }
    class MyListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state){
                case TelephonyManager.CALL_STATE_RINGING://电话铃声响了
                    System.out.println("电话铃响了");
                    String addressByNumber = AddressDao.getAddressByNumber(incomingNumber);//根据电话号码查询归属地
                    Toast.makeText(AddressService.this,addressByNumber,Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    }

}
