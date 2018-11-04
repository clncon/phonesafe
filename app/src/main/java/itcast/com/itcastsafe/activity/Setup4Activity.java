package itcast.com.itcastsafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import itcast.com.itcastsafe.R;

public class Setup4Activity extends BaseSetupActivity {

    private CheckBox cb_protect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        cb_protect = findViewById(R.id.cb_protect);

        boolean protect = mConfig.getBoolean("protect", false);
        if(protect){
            cb_protect.setText("防盗保护已经开启");
            cb_protect.setChecked(true);
        }else{
            cb_protect.setText("防盗保护没有开启");
            cb_protect.setChecked(false);

        }
        cb_protect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cb_protect.setText("防盗保护已经开启");
                    mConfig.edit().putBoolean("protect",true).commit();
                }else{
                    cb_protect.setText("防盗保护没有开启");
                    mConfig.edit().putBoolean("protect",false).commit();

                }
            }
        });
    }

    @Override
    public void showPreviousPage() {
        startActivity(new Intent(Setup4Activity.this,Setup3Activity.class));
        finish();
        /**
         * 两个界面切换的动画
         */
        /**
         * 两个界面切换的动画
         */
        overridePendingTransition(R.anim.previous_in,R.anim.previous_out);
    }

    @Override
    public void showNextPage() {

    }


    @Override
    public void next(View v) {
        mConfig.edit().putBoolean("configed",true).commit();
        startActivity(new Intent(Setup4Activity.this,LostFindActivity.class));
        finish();
        /**
         * 两个界面切换的动画
         */
        overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setup4, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
