package itcast.com.itcastsafe.activity.service;

import android.app.Service;
import android.content.*;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.ITelephony;
import com.safframework.log.L;
import itcast.com.itcastsafe.activity.dao.BlackNumberDao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CallSafeService extends Service {

    private TelephonyManager tm;

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
        registerReceiver(innerReceiver, intentFilter);

        //获取到系统的电话服务
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        MyPhoneStateListener listener = new MyPhoneStateListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);


    }

    private class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            //            * @see TelephonyManager#CALL_STATE_IDLE  电话闲置
//            * @see TelephonyManager#CALL_STATE_RINGING 电话铃响的状态
//            * @see TelephonyManager#CALL_STATE_OFFHOOK 电话接通

            switch (state) {
                //电话铃声响的状态
                case TelephonyManager.CALL_STATE_RINGING:
                    String mode = dao.findNumber(incomingNumber);
                    /**
                     * 黑名单拦截模式
                     * 1 全部拦截 电话拦截 + 短信拦截
                     * 2 电话拦截
                     * 3 短信拦截
                     */
                    System.out.println("number" + incomingNumber + ":" + mode);
                    if ("1".equals(mode) || "2".equals(mode)) {
                        System.out.println("黑名单电话挂断");
                        Uri uri = Uri.parse("content://call_log/calls");
                       getContentResolver().registerContentObserver(uri,true,new MyContentObserver(new Handler(),incomingNumber));
                        endCall();
                    }

                    break;
            }
        }
    }

    /**
     * 挂断电话
     */
    private void endCall() {
        //通过类加载器加载ServiceManager
        try {
            Class<?> clazz = getClassLoader().loadClass("android.os.ServiceManager");
            //通过反射得到当前方法
            Method method = clazz.getDeclaredMethod("getService", String.class);
            IBinder iBinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
            ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
            iTelephony.endCall();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyContentObserver extends ContentObserver {
         String incomingNumber;

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyContentObserver(Handler handler,String incomingNumber) {
            super(handler);
            this.incomingNumber=incomingNumber;
        }

        @Override
        public void onChange(boolean selfChange) {
            getContentResolver().unregisterContentObserver(this);
            deleteCallLog(incomingNumber);
            super.onChange(selfChange);
        }
    }

    //删除电话号码
    private void deleteCallLog(String incomingNumber) {
        Uri uri = Uri.parse("content://call_log/calls");
        getContentResolver().delete(uri,"number=?",new String[]{incomingNumber});
    }

    private class InnerReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objects) {
                SmsMessage message = SmsMessage.createFromPdu((byte[]) obj);
                String originatingAddress = message.getOriginatingAddress();//来电短信的来源号码
                String messageBody = message.getMessageBody();
                System.out.println(originatingAddress + ":" + messageBody);
                //通过短信电话号码查询拦截模式
                String mode = dao.findNumber(originatingAddress);

                if ("1".equals(mode)) {
                    abortBroadcast();
                } else if ("3".equals(mode)) {
                    abortBroadcast();
                }
            }
        }
    }
}
