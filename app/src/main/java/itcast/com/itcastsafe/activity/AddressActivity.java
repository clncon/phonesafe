package itcast.com.itcastsafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import itcast.com.itcastsafe.R;
import itcast.com.itcastsafe.activity.dao.AddressDao;

public class AddressActivity extends Activity {


    private EditText et_number;
    private String phoneNumber;
    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_);
        et_number = findViewById(R.id.et_number);
        tv_result = findViewById(R.id.tv_result);
        et_number.addTextChangedListener(new TextWatcher() {
            //文本改变之前的method
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            //文本改变时的method
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String addressByNumber = AddressDao.getAddressByNumber(s.toString());
                tv_result.setText(addressByNumber);
            }
            //文本改变之后的method
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    //查询归属地
    public void query(View v){
        phoneNumber = et_number.getText().toString();

        if(TextUtils.isEmpty(phoneNumber)){
           //抖动和震动
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            et_number.startAnimation(shake);
            vibrate();

            return;
        }
        String addressByNumber = AddressDao.getAddressByNumber(phoneNumber);

        tv_result.setText(addressByNumber);
    }


    public void vibrate(){
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{1000,2000,1000,3000},-1);// 先等待1秒,再震动2秒,再等待1秒,再震动3秒,
        // 参2等于-1表示只执行一次,不循环,
        // 参2等于0表示从头循环,
        // 参2表示从第几个位置开始循环

    }

}
