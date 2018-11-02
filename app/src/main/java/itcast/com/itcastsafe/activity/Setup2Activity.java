package itcast.com.itcastsafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import itcast.com.itcastsafe.R;

public class Setup2Activity extends BaseSetupActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

    }



    public void showPreviousPage(){
        startActivity(new Intent(Setup2Activity.this,Setup1Activity.class));
        finish();
        /**
         * 两个界面切换的动画
         */
        overridePendingTransition(R.anim.previous_in,R.anim.previous_out);

    }

    public void showNextPage(){
        startActivity(new Intent(Setup2Activity.this,Setup3Activity.class));
        finish();
        /**
         * 两个界面切换的动画
         */
        overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setup2, menu);
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
