package itcast.com.itcastsafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import itcast.com.itcastsafe.R;

public class HomeActivity extends Activity {

    private GridView gvHome;
    private String mItems[]= new String[]{"手机防盗", "通讯卫士", "软件管理", "进程管理",
            "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"};
    private int mPics[] = new int[]{ R.mipmap.home_safe,
            R.mipmap.home_callmsgsafe, R.mipmap.home_apps,
            R.mipmap.home_taskmanager, R.mipmap.home_netmanager,
            R.mipmap.home_trojan, R.mipmap.home_sysoptimize,
            R.mipmap.home_tools, R.mipmap.home_settings};

    private SharedPreferences config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = getSharedPreferences("config", MODE_PRIVATE);
        setContentView(R.layout.activity_home);
        gvHome = findViewById(R.id.gv_home);
        gvHome.setAdapter(new HomeAdapter());


        //设置监听

       gvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               switch (position){
                   case 8:
                       //设置中心
                       startActivity(new Intent(HomeActivity.this,SettingActivity.class));
                       break;

                   case 0:
                       //手机防盗
                       String password = config.getString("password", null);
                       if(TextUtils.isEmpty(password)){
                           showPasswordConfrimDialog();
                       }else{
                          showInputPasswordDialog();
                       }
                       break;
               };

           }
       });
    }

    /**
     * 展示输入密码的对话框
     */
    private void showInputPasswordDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
        final View dailog_input_password = View.inflate(HomeActivity.this, R.layout.dailog_input_password, null);
        Button bt_ok = dailog_input_password.findViewById(R.id.bt_ok);
        Button bt_cancel = dailog_input_password.findViewById(R.id.bt_cancel);
        alertDialog.setView(dailog_input_password);//将布局文件内容交给alertDialog

        final AlertDialog dia = alertDialog.show();


        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_password = dailog_input_password.findViewById(R.id.et_password);
                String inputPassword = et_password.getText().toString();
                String password = config.getString("password", null);
                if(password.equals(inputPassword)){
                    Toast.makeText(HomeActivity.this,"密码正确",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(HomeActivity.this,"密码错误",Toast.LENGTH_SHORT).show();

                }


            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia.dismiss();
            }
        });
    }
    /**
     * 展示设置密码的对话框
     */
    private void showPasswordConfrimDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
        final View dailog_set_password = View.inflate(HomeActivity.this, R.layout.dailog_set_password, null);
        Button bt_ok = dailog_set_password.findViewById(R.id.bt_ok);
        Button bt_cancel = dailog_set_password.findViewById(R.id.bt_cancel);
        alertDialog.setView(dailog_set_password);//将布局文件内容交给alertDialog

        final AlertDialog dia = alertDialog.show();


        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_password = dailog_set_password.findViewById(R.id.et_password);
                EditText et_confrimpassword = dailog_set_password.findViewById(R.id.et_confrimpassword);
                String password = et_password.getText().toString();
                String confirmpassword = et_confrimpassword.getText().toString();
                if(TextUtils.isEmpty(password)||TextUtils.isEmpty(confirmpassword)){
                    Toast.makeText(HomeActivity.this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                }else if(!password.equals(confirmpassword)){
                    Toast.makeText(HomeActivity.this,"两次输入必须一致",Toast.LENGTH_SHORT).show();
                }else{
                       config.edit().putString("password",password).commit();
                       Toast.makeText(HomeActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
                       dia.dismiss();
                }

            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dia.dismiss();
            }
        });
    }




    class HomeAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            return mItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           View view =  View.inflate(HomeActivity.this,R.layout.home_list_item,null);
            ImageView iv_item = view.findViewById(R.id.iv_item);
            TextView tv_item = view.findViewById(R.id.tv_item);
            iv_item.setImageResource(mPics[position]);
            tv_item.setText(mItems[position]);


            return view;
        }
    }

}
