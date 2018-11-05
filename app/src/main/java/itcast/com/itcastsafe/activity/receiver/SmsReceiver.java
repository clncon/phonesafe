package itcast.com.itcastsafe.activity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import itcast.com.itcastsafe.R;

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
            }
        }

    }
}
