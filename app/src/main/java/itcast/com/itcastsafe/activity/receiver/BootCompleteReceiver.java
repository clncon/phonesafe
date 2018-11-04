package itcast.com.itcastsafe.activity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        System.out.println("广播被调用了：");
        SharedPreferences config = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String sim = config.getString("sim", null);
        if(!TextUtils.isEmpty(sim)){
            TelephonyManager te = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String simSerialNumber = te.getSimSerialNumber();
            if(sim.equals(simSerialNumber)){
                System.out.println("手机安全");
                Toast.makeText(context,"手机安全",Toast.LENGTH_SHORT).show();
            }else{
                System.out.println("手机危险，sim已经发生了变化");
                Toast.makeText(context,"手机危险，sim已经发生了变化",Toast.LENGTH_SHORT).show();

            }
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
