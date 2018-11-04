package itcast.com.itcastsafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import itcast.com.itcastsafe.R;
import itcast.com.itcastsafe.activity.utils.ToastUtil;

public class Setup3Activity extends BaseSetupActivity {

    private EditText et_phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        et_phone = findViewById(R.id.et_phone);
        String phoneNum = mConfig.getString("phone", "");
        et_phone.setText(phoneNum);

    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(Setup3Activity.this,Setup2Activity.class));
        finish();
        /**
         * 两个界面切换的动画
         */
        overridePendingTransition(R.anim.previous_in,R.anim.previous_out);
    }

    @Override
    public void showNextPage() {

        String phone = et_phone.getText().toString().trim();//过滤空格
        if(TextUtils.isEmpty(phone)){
            ToastUtil.showToast(Setup3Activity.this,"手机号不能为空");
            return;
        }
        mConfig.edit().putString("phone",phone).commit();
        startActivity(new Intent(Setup3Activity.this,Setup4Activity.class));
        finish();
        /**
         * 两个界面切换的动画
         */
        overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
    }

    public void selectContact(View v){
        Intent intent = new Intent(Setup3Activity.this,ContactActivity.class);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            String phone  = extras.getString("phone");
            if(!TextUtils.isEmpty(phone)){
                phone = phone.replaceAll("-","").replaceAll(" ","");
                 et_phone.setText(phone);
                mConfig.edit().putString("phone",phone).commit();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
