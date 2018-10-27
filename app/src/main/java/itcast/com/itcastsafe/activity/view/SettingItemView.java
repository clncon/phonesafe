package itcast.com.itcastsafe.activity.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import itcast.com.itcastsafe.R;
import itcast.com.itcastsafe.activity.SettingActivity;

/**
 * @Author:MingKong
 * @Description:
 * @Date:Created in 20:10 2018/10/27
 * @Modified By:
 */
@TargetApi(21)
public class SettingItemView extends RelativeLayout{
    private  TextView tv_title;
    private  TextView tv_desc;
    private CheckBox checkBox;
    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView(){
        //将自定义好的布局文件设置给当前的SettingItemView
        View setting_item = View.inflate(getContext(), R.layout.view_setting_item,this);
        checkBox  = setting_item.findViewById(R.id.cb_status);
        tv_title = setting_item.findViewById(R.id.tv_title);
        tv_desc = setting_item.findViewById(R.id.tv_desc);
    }

    public void setTv_title(String title){
     tv_title.setText(title);
    }

    public void setTv_desc(String desc){
        tv_desc.setText(desc);
    }

    public boolean isChecked() {
        return checkBox.isChecked();
    }

    public void setIsChecked(boolean flag) {
        this.checkBox.setChecked(flag);
    }
}
