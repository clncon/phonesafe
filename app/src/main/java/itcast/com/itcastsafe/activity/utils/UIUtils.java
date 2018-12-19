package itcast.com.itcastsafe.activity.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * @Author:MingKong
 * @Description:
 * @Date:Created in 0:22 2018/12/20
 * @Modified By:
 */
public class UIUtils {
    public static void showToast(final Activity context, final String msg){
        if("main".equals(Thread.currentThread().getName())){
            Toast.makeText(context, msg, 1).show();
        }else{
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, msg, 1).show();
                }
            });
        }
    }
}
