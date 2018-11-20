package itcast.com.itcastsafe.activity.service;

import android.app.Service;
import android.content.*;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import itcast.com.itcastsafe.R;
import itcast.com.itcastsafe.activity.dao.AddressDao;

public class AddressService extends Service {

    private TelephonyManager tm;
    private MyListener myListener;
    private OutCallReceiver receiver;
    private WindowManager mWM;
    private View view;
    SharedPreferences config;
    private int winWidth;
    private int winHeight;
    private int startX;
    private int startY;
    private int endX;
    private WindowManager.LayoutParams params;

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
        config = getSharedPreferences("config", MODE_PRIVATE);
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
                    showToast(addressByNumber);
                    break;
                case TelephonyManager.CALL_STATE_IDLE://电话出于闲置的状态
                    if(mWM!=null&&view!=null){
                        mWM.removeView(view);
                        view=null;// 从window中移除view
                    }
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
            showToast(addressByNumber);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tm.listen(myListener,PhoneStateListener.LISTEN_NONE);//停止来电接听

        unregisterReceiver(receiver);
    }


    /**
     * 自定义归属地浮窗
     * @param text
     */
    public void showToast(String text){
        mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        //获取屏幕的宽高
        winWidth = mWM.getDefaultDisplay().getWidth();
        winHeight = mWM.getDefaultDisplay().getHeight();
        params = new WindowManager.LayoutParams();
        params.height=WindowManager.LayoutParams.WRAP_CONTENT;
        params.width=WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                 |WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format= PixelFormat.TRANSLUCENT;
        params.type=WindowManager.LayoutParams.TYPE_PHONE;//电话窗口。它用于电话交互（特别是呼入）。它置于所有应用程序之上，状态栏之下
        params.gravity= Gravity.LEFT+Gravity.TOP;//将重心位置放到左上方，也就是(0,0)从左上方开始，而不是默认的中心位置
        params.setTitle("Toast");
        int lastX = config.getInt("lastX",0);
        int lastY = config.getInt("lastY",0);
        //设置浮窗的位置，基于左上方的偏移量
        params.x=lastX;
        params.y=lastY;


        int[] bgs = new int[]{R.mipmap.call_locate_white,R.mipmap.call_locate_orange,R.mipmap.call_locate_blue,R.mipmap.call_locate_gray,R.mipmap.call_locate_green};
        view = View.inflate(this,R.layout.toast_address,null);
        final int style = config.getInt("AdressStyle", 0);
        System.out.println("style:"+style);
        view.setBackgroundResource(bgs[style]);//根据存储的样式来更新背景
        TextView tv_number = view.findViewById(R.id.tv_number);
        tv_number.setText(text);
        mWM.addView(view, params);//将view添加到屏幕
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.print("onTouch....");
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        System.out.print(startX+":"+startY);
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();

                        //计算偏移量
                        int dx = endX-startX;
                        int dy = endY-startY;

                        //更新浮框位置
                        params.x+=dx;
                        params.y+=dy;
                       //防止坐标偏离屏幕
                        if(params.x<0){
                            params.x=0;
                        }

                        if(params.y<0){
                            params.y=0;
                        }

                        if(params.x>winWidth-view.getWidth()){
                            params.x=winWidth-view.getWidth();
                        }

                        if(params.y>winHeight-view.getHeight()){
                            params.y=winHeight-view.getHeight();
                        }

                        mWM.updateViewLayout(view,params);
                        //重新初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                    case MotionEvent.ACTION_UP:
                        SharedPreferences.Editor edit = config.edit();
                        edit.putInt("lastX",params.x);
                        edit.putInt("lastY",params.y);
                        edit.commit();
                        break;

                    default:
                        break;
                }
                return true;
            }
        });

    }



}
