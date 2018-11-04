package itcast.com.itcastsafe.activity.utils;

import android.content.Context;
import android.widget.Toast;
import itcast.com.itcastsafe.activity.Setup3Activity;

/**
 * @Author:MingKong
 * @Description:
 * @Date:Created in 1:45 2018/11/5
 * @Modified By:
 */
public class ToastUtil {

    public static void showToast(Context context, String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();

    }
}
