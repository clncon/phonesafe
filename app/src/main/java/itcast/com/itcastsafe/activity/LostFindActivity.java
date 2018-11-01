package itcast.com.itcastsafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import itcast.com.itcastsafe.R;

public class LostFindActivity extends Activity {

    private SharedPreferences config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        config = getSharedPreferences("config", MODE_PRIVATE);
        boolean configed = config.getBoolean("configed",false);
        if(configed){
            //设置过手机防盗
            setContentView(R.layout.activity_lost_find);
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
