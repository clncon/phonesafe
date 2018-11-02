package itcast.com.itcastsafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import itcast.com.itcastsafe.R;

public class Setup4Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
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
        getSharedPreferences("config",MODE_PRIVATE).edit().putBoolean("configed",true).commit();
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
