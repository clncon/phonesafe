package itcast.com.itcastsafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import itcast.com.itcastsafe.R;

public class LostFindActivity extends Activity {

    private SharedPreferences config;
    private ImageView iv_protect;
    private TextView tv_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = getSharedPreferences("config", MODE_PRIVATE);
        boolean configed = config.getBoolean("configed",false);
        if(configed){
            //设置过手机防盗
            setContentView(R.layout.activity_lost_find);
            iv_protect = findViewById(R.id.iv_protect);
            tv_phone = findViewById(R.id.tv_phone);
            //从shareConifg中获取安全号码和设置防盗保护打开
            boolean protect = config.getBoolean("protect", false);//是否保护开启
            String phone = config.getString("phone", "");//安全号码
            tv_phone.setText(phone);
            if(protect){
                iv_protect.setImageResource(R.mipmap.lock);
            }else{

                iv_protect.setImageResource(R.mipmap.unlock);
            }
        }else{

            //没有设置过手机防盗
            startActivity(new Intent(getApplicationContext(),Setup1Activity.class));
            finish();

        }


    }

    public void reEnter(View v){

        startActivity(new Intent(LostFindActivity.this,Setup1Activity.class));
        finish();

    }


}
