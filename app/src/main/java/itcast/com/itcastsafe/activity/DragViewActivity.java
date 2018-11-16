package itcast.com.itcastsafe.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
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

        iv_drag.setLayoutParams(layoutParams);//重新设置位置
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
                return true;
            }
        });

    }


}
