package itcast.com.itcastsafe.activity.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import itcast.com.itcastsafe.activity.dao.AddressDao;

public class AddressService extends Service {

    private TelephonyManager tm;
    private MyListener myListener;
    private OutCallReceiver receiver;

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
        myListener = new MyListener();
        tm.listen(myListener,PhoneStateListener.LISTEN_CALL_STATE);//监听来电状态

        receiver = new OutCallReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(receiver,filter);
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

    class OutCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: This method is called when the BroadcastReceiver is receiving
            String number = getResultData();//获取电话号码
            String addressByNumber = AddressDao.getAddressByNumber(number);
            Toast.makeText(context,addressByNumber,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tm.listen(myListener,PhoneStateListener.LISTEN_NONE);//停止来电接听

        unregisterReceiver(receiver);
    }
}
