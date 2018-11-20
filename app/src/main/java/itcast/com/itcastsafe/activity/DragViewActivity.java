package itcast.com.itcastsafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import itcast.com.itcastsafe.R;

public class DragViewActivity extends Activity {

    private TextView tv_top;
    private TextView tv_bottom;
    private ImageView iv_drag;
    private int startY;
    private int startX;
    private SharedPreferences config;
    long[] mHits = new long[2];//数组长度标识点击的次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drag_view);
        config = getSharedPreferences("config", MODE_PRIVATE);
        tv_top = findViewById(R.id.tv_top);
        tv_bottom = findViewById(R.id.tv_bottom);
        iv_drag = findViewById(R.id.iv_drag);
        int lastX = config.getInt("lastX",0);
        int lastY = config.getInt("lastY",0);
        // onMeasure(测量view), onLayout(安放位置), onDraw(绘制)
        // ivDrag.layout(lastX, lastY, lastX + ivDrag.getWidth(),
        // lastY + ivDrag.getHeight());//不能用这个方法,因为还没有测量完成,就不能安放位置
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) iv_drag.getLayoutParams();//获取布局对象
        layoutParams.leftMargin=lastX;//设置左边距
        layoutParams.topMargin=lastY;//设置top边距

        //获取屏幕的宽高
        final int winWidth = getWindowManager().getDefaultDisplay().getWidth();
        final int winHeight = getWindowManager().getDefaultDisplay().getHeight();
        iv_drag.setLayoutParams(layoutParams);//重新设置位置

        iv_drag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();// 开机后开始计算的时间
                if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
                    // 把图片居中
                    iv_drag.layout(winWidth / 2 - iv_drag.getWidth() / 2,
                            iv_drag.getTop(), winWidth / 2 + iv_drag.getWidth()
                                    / 2, iv_drag.getBottom());
                }
            }
        });
        //设置触摸事件
        iv_drag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();
                        //计算偏离量
                        int dx = endX - startX;
                        int dy = endY - startY;
                        //更新左上右下距离
                        int l = iv_drag.getLeft() + dx;
                        int r = iv_drag.getRight() + dx;
                        int t = iv_drag.getTop() + dy;
                        int b = iv_drag.getBottom() + dy;
                        //判断是否超出屏幕边界，注意状态栏的高度
                        if(l<0||r>winWidth||t<0||b>winHeight-20){
                            break;
                        }

                        //根据图片的位置，决定提示框显示或者隐藏
                         if(t>winHeight/2){
                            //上边显示，下边隐藏
                             tv_top.setVisibility(View.VISIBLE);
                             tv_bottom.setVisibility(View.INVISIBLE);
                         }else{
                            tv_top.setVisibility(View.INVISIBLE);
                            tv_bottom.setVisibility(View.VISIBLE);
                         }
                        //更新界面
                        iv_drag.layout(l, t, r, b);
                        //重新初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        SharedPreferences.Editor editor = config.edit();
                        editor.putInt("lastX", (int) event.getRawX());
                        editor.putInt("lastY", (int) event.getRawY());
                        editor.commit();
                        break;
                    default:
                        break;

                }
                return false;
            }
        });

    }



}
