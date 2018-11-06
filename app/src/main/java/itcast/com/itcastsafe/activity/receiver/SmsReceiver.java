package itcast.com.itcastsafe.activity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import itcast.com.itcastsafe.R;
import itcast.com.itcastsafe.activity.service.LocationService;
import itcast.com.itcastsafe.activity.service.SystemService;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Object[] objects = (Object[]) intent.getExtras().get("pdus");
        for(Object obj:objects){
            SmsMessage message = SmsMessage.createFromPdu((byte[]) obj);
            String originatingAddress = message.getOriginatingAddress();//来电短信的来源号码
            String messageBody = message.getMessageBody();
            System.out.println(originatingAddress+":"+messageBody);
            if("#*alarm*#".equals(messageBody)){
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                mediaPlayer.setVolume(1f,1f);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                abortBroadcast();//中断短信的传递
            }else if("#*loaction*#".equals(messageBody)){
                //获取经纬度坐标
                context.startService(new Intent(context,LocationService.class));
                SharedPreferences config = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                String location = config.getString("location", "gettiing location...");
                System.out.println("location:"+location);
                abortBroadcast();
            }else if("#*wipedata*#".equals(messageBody)){
                Intent it = new Intent(context, SystemService.class);
                SharedPreferences config = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                config.edit().putString("type","wipedata").commit();
                context.startService(it);


            }else if("#*lockscreen*#".equals(messageBody)){
                Intent it = new Intent(context, SystemService.class);
                SharedPreferences config = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                config.edit().putString("type","lockscreen").commit();
                context.startService(it);
            }
        }

    }
}
