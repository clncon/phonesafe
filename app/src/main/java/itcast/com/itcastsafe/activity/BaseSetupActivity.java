package itcast.com.itcastsafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class BaseSetupActivity extends Activity {
    private GestureDetector mDetector;
    public SharedPreferences mConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mConfig = getSharedPreferences("config", MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        mDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener(){

            //监听手势滑动
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //判断用户是否上下滑动
                if(Math.abs(e1.getRawY()-e2.getRawY())>100)return true;
                //判断是否滑动的太慢了
                if(Math.abs(velocityX)<50)return true;
                //向右滑，跳上一页
                if(e2.getRawX()-e1.getRawX()>100){
                    showPreviousPage();
                    //向左滑，跳下一页
                }else if(e1.getRawX()-e2.getRawX()>100){
                    showNextPage();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

    }

    public  abstract void showPreviousPage();

    public  abstract void showNextPage();


    public void next(View v){
        showNextPage();
    }

    public void previous(View v){
        showPreviousPage();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);//委託手勢識別器處理觸摸事件
        return super.onTouchEvent(event);
    }
}
