package itcast.com.itcastsafe.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.*;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import itcast.com.itcastsafe.BuildConfig;
import itcast.com.itcastsafe.R;
import itcast.com.itcastsafe.activity.utils.StreamUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;


public class SplashActivity extends Activity {
    protected static final int CODE_UPDATE_DIALOG=0;
    protected static final int CODE_URL_ERROR=1;
    protected static final int CODE_NET_ERROR=2;
    protected static final int CODE_JSON_ERROR=3;
    protected static final int CODE_ENTER_HOME=4;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };
    private static String[] PERMISSIONS_CONTACTS = {
             Manifest.permission.READ_CONTACTS

    };
    private static String[] PERMISSIONS_SEND_SMS = {
             Manifest.permission.SEND_SMS,
             Manifest.permission.RECEIVE_SMS,
             Manifest.permission.RECEIVE_BOOT_COMPLETED

    };
    private static String[] PERMISSIONS_LOCATION = {
             Manifest.permission.ACCESS_FINE_LOCATION,
             Manifest.permission.ACCESS_COARSE_LOCATION

    };

    private static int REQUEST_PERMISSION_CODE = 0;
    private String mVersionName;
    private int mVersionCode;
    private String mDesc;
    private String mDownloadUrl;
    private TextView mTv_progress;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

           switch (msg.what){
               case CODE_UPDATE_DIALOG:
                   //显示下载更新弹窗
                   System.out.println("弹框");
                   showUpdateDialog();
                   break;
               case CODE_NET_ERROR:
                   //显示网络错误
                   Toast.makeText(SplashActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                   entryHome();
                   break;
               case CODE_JSON_ERROR:
                   //数据格式错误
                   Toast.makeText(SplashActivity.this,"数据格式错误",Toast.LENGTH_SHORT).show();
                   entryHome();
                   break;
               case CODE_URL_ERROR:
                   //url错误
                   Toast.makeText(SplashActivity.this,"url错误",Toast.LENGTH_SHORT).show();
                   entryHome();
                   break;
               case CODE_ENTER_HOME:
                   entryHome();
                   break;
               default:
                   break;
           }

        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String versionName = getVersionName();
        //System.out.println("versionName:"+versionName);
        TextView tv_version  = findViewById(R.id.tv_version);
        tv_version.setText("版本:"+versionName);//动态设置版本信息
        mTv_progress = findViewById(R.id.tv_progress);//默认隐藏
        SharedPreferences config = getSharedPreferences("config", MODE_PRIVATE);
        copyData("address.db");
        if(config.getBoolean("auto_update",true)){
            checkVersion();

        }else {
            mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME,2000);
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_CONTACTS, REQUEST_PERMISSION_CODE);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_SEND_SMS, REQUEST_PERMISSION_CODE);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION, REQUEST_PERMISSION_CODE);
            }

            if(Build.VERSION.SDK_INT > 8){
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
        }

        RelativeLayout rl_Root = findViewById(R.id.rl_Root);
        //渐变的动画效果
        AlphaAnimation animation = new AlphaAnimation(0.1f,1f);
        animation.setDuration(2000);
        rl_Root.startAnimation(animation);


    }

    /*
    * 申请读取/写入文件的权限
    * */
    @Override
    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }
    /**
     * 进入主页
     */
    private void entryHome(){
       Intent intent = new Intent(this,HomeActivity.class);
       startActivity(intent);
       finish();
    }
    /*显示更新对话框*/
    private void showUpdateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("最新版本:"+mVersionName);
        builder.setMessage(mDesc);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("立即更新");
                downlod();
            }
        });

        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //System.out.println("以后再说");
                entryHome();
            }
        });

        /**
         * 设置取消的监听，用户点击返回键时会触发
         */
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                  entryHome();
            }
        });

        builder.show();
    }

    private  String getVersionName(){
        String versionName="";
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);//获取包信息
            versionName = packageInfo.versionName;
            System.out.println("versionName:"+versionName);
        } catch (PackageManager.NameNotFoundException e) {
            //没有找到包名抛出异常
            e.printStackTrace();
        }

        return versionName;


    }

    private Integer getVersionCode(){
        Integer versionCode = null;
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);//获取包信息
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            //没有找到包名抛出异常
            e.printStackTrace();
        }

        return versionCode;


    }
    String url = "http://118.25.188.72:8080/update.json";

    /**
     * 检查版本信息更改
     */
    private void checkVersion(){
        final long startTime = System.currentTimeMillis();
        new Thread(){
           @Override
           public void run() {
               HttpURLConnection connection=null;
               Message  message = Message.obtain();
               try {

                   //构建URL
                   URL url = new URL("http://118.25.188.72:8080/update.json");
                   //取得连接对象
                     connection = (HttpURLConnection) url.openConnection();
                   //设置连接参数
                   connection.setReadTimeout(5000);//响应超时时间
                   connection.setConnectTimeout(5000);//连接超时时间
                   connection.setRequestMethod("GET");//请求的方法
                   connection.connect();//获取连接
                   int responseCode = connection.getResponseCode();//取得响应码
                   if(responseCode==200){
                       //相映成功
                      InputStream is =  connection.getInputStream();//获取输入流
                       String result = StreamUtils.readFromStream(is);
                       System.out.println(result);
                       JSONObject jsonObject = new JSONObject(result);
                        mVersionCode = jsonObject.getInt("versionCode");
                        mVersionName = jsonObject.getString("versionName");
                        mDownloadUrl = jsonObject.getString("downloadUrl");
                        mDesc = jsonObject.getString("description");
                       if(mVersionCode>getVersionCode()){
                           //如果网络上的versionCode大于本地的versionCode，说明系统存在新版本
                           message.what = SplashActivity.CODE_UPDATE_DIALOG;
                       }else {
                           message.what=SplashActivity.CODE_ENTER_HOME;
                       }
                   }

               } catch (MalformedURLException e) {
                   //url错误
                   message.what = SplashActivity.CODE_URL_ERROR;
                   e.printStackTrace();
               } catch (IOException e) {
                   //网络错误
                   message.what = SplashActivity.CODE_NET_ERROR;
                   e.printStackTrace();
               } catch (JSONException e) {
                   //格式错误
                   message.what = SplashActivity.CODE_JSON_ERROR;
                   e.printStackTrace();
               } finally {
                   long endTime = System.currentTimeMillis();
                   long timeUsed = endTime - startTime;
                   if(timeUsed<2000){
                       //强制休眠，保证闪屏页面展示2s
                       try {
                           Thread.sleep(2000-timeUsed);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
                   mHandler.sendMessage(message);
                 if(connection!=null){
                     connection.disconnect();
                 }
               }
           }
       }.start();
    }

    /**
     * 下载apk文件
     */
    private void downlod(){


          if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
              mTv_progress.setVisibility(View.VISIBLE);//显示进度
              String target = Environment.getExternalStorageDirectory()+"/upload.apk";
              HttpUtils httpUtils = new HttpUtils();
              httpUtils.download(mDownloadUrl, target, new RequestCallBack<File>() {

                  //下载文件的进度
                  @Override
                  public void onLoading(long total, long current, boolean isUploading) {
                      super.onLoading(total, current, isUploading);
                      mTv_progress.setText("下载进度："+current*100/total+"%");
                  }

                  //下载成功
                  @Override
                  public void onSuccess(ResponseInfo<File> responseInfo) {
                      //跳转到系统下载页面
                      Intent intent = new Intent(Intent.ACTION_VIEW);
                      intent.addCategory(Intent.CATEGORY_DEFAULT);
                      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                          intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                          Uri contentUri = FileProvider.getUriForFile(SplashActivity.this, BuildConfig.APPLICATION_ID + ".fileProvider", responseInfo.result);
                          intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                      } else {
                          intent.setDataAndType(Uri.fromFile(responseInfo.result), "application/vnd.android.package-archive");
                          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                      }

                      //startActivity(intent);
                      startActivityForResult(intent,0);//如果用户取消安装的话，
                      // 会返回结果，回调方法OnActivityResult

                  }
                  //下载失败
                  @Override
                  public void onFailure(HttpException e, String s) {
                     Toast.makeText(SplashActivity.this,"下载失败！",Toast.LENGTH_SHORT).show();
                     entryHome();
                  }
              });

          }else{
              Toast.makeText(SplashActivity.this,"没有找到SD!",Toast.LENGTH_SHORT).show();
          }



    }

    /**
     * 用户点击取消安装时调用此方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        entryHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

     /*   final String myPackageName = getPackageName();
        if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {
            System.out.println(Telephony.Sms.getDefaultSmsPackage(this));
            Intent intent =
                    new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                    myPackageName);
            startActivity(intent);


        }*/



    }
    //拷贝数据库
    public void copyData(String database){
        File file = new File(getFilesDir(),database);
        InputStream is =null;
        OutputStream os=null;
                if(!file.exists()){

                    try {
                        is = getAssets().open(database);
                        os = new FileOutputStream(file);
                        byte[] buff = new byte[1024];
                        int len=0;
                        while((len=is.read(buff))!=-1){
                            System.out.println("copy....."+buff.length);
                            os.write(buff,0,len);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        if(is!=null){
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        if(os!=null){
                            try {
                                os.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }

    }
}
