package itcast.com.itcastsafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import itcast.com.itcastsafe.R;
import itcast.com.itcastsafe.activity.utils.SmsUtils;
import itcast.com.itcastsafe.activity.utils.ToastUtil;
import itcast.com.itcastsafe.activity.utils.UIUtils;

public class AToolsActivity extends Activity {
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
        progressBar = findViewById(R.id.progressbar1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.atools, menu);
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

    public void numberAddressQuery(View v){
        startActivity(new Intent(AToolsActivity.this,AddressActivity.class));
    }

    public void backupsms(View v){

        final ProgressDialog progressDialog = new ProgressDialog(AToolsActivity.this);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("正在备份中，请稍后");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
       // progressDialog.show();
        new Thread(){
            @Override
            public void run() {
                super.run();
                boolean flag =SmsUtils.INSTANCE.backup(AToolsActivity.this,new SmsUtils.BackUpSms(){

                    @Override
                    public void before(int count) {
                       progressBar.setMax(count);
                    }

                    @Override
                    public void onBackUpSms(int progress) {
                        progressBar.setProgress(progress);
                    }


                });
                if(flag){
                   /* Looper.prepare();
                    ToastUtil.showToast(AToolsActivity.this,"短信备份成功");
                    Looper.loop();*/
                    UIUtils.showToast(AToolsActivity.this,"短信备份成功");
                }else{
                 /*   Looper.prepare();
                    ToastUtil.showToast(AToolsActivity.this,"短信备份成功");
                    Looper.loop();*/
                    UIUtils.showToast(AToolsActivity.this,"短信备份失败");

                }
                progressDialog.dismiss();
            }
        }.start();


    }
}
