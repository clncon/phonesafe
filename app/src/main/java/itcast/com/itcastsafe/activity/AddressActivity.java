package itcast.com.itcastsafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import itcast.com.itcastsafe.R;
import itcast.com.itcastsafe.activity.dao.AddressDao;

public class AddressActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_);
    }


    //查询归属地
    public void query(View v){

        EditText et_number = findViewById(R.id.et_number);
        String phoneNumber = et_number.getText().toString();
        if(TextUtils.isEmpty(phoneNumber))return;
        String addressByNumber = AddressDao.getAddressByNumber(phoneNumber);
        TextView tv_result = findViewById(R.id.tv_result);
        tv_result.setText(addressByNumber);
    }

}
