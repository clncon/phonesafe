package itcast.com.itcastsafe.activity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import itcast.com.itcastsafe.activity.dao.AddressDao;

public class OutCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        String number = getResultData();//获取电话号码
        String addressByNumber = AddressDao.getAddressByNumber(number);
        Toast.makeText(context,addressByNumber,Toast.LENGTH_LONG).show();
    }
}
