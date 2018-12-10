package itcast.com.itcastsafe.activity.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsMessage;
import itcast.com.itcastsafe.activity.dao.BlackNumberDao;

public class CallSafeService extends Service {
    public CallSafeService() {
    }
    BlackNumberDao dao;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        dao = new BlackNumberDao(this);
        //初始化短信广播
        InnerReciver innerReceiver = new InnerReciver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED_2");
        intentFilter.addAction("android.provider.Telephony.GSM_SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(innerReceiver,intentFilter);

    }

    private class InnerReciver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            for(Object obj:objects) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) obj);
                String originatingAddress = message.getOriginatingAddress();//来电短信的来源号码
                String messageBody = message.getMessageBody();
                System.out.println(originatingAddress + ":" + messageBody);
                //通过短信电话号码查询拦截模式
                String mode = dao.findNumber(originatingAddress);

                if("1".equals(mode)){
                     abortBroadcast();
                }else if("3".equals(mode)){
                    abortBroadcast();
                }
            }
        }
    }
}
