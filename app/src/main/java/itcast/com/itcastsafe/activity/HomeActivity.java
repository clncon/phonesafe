package itcast.com.itcastsafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
               };

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
