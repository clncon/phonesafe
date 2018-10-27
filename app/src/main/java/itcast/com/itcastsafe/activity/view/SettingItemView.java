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
    private final static String NAMESPACE="http://schemas.android.com/apk/res-auto";
    private  TextView tv_title;
    private  TextView tv_desc;
    private CheckBox checkBox;
    private String title;
    private String desc_Off;
    private String desc_On;
    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

       title = attrs.getAttributeValue(NAMESPACE, "title");
         desc_Off = attrs.getAttributeValue(NAMESPACE, "desc_Off");
        desc_On = attrs.getAttributeValue(NAMESPACE, "desc_On");
        //System.out.println("title:"+title);
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
        setTv_title(title);


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
        //设置状态文本描述
        if(flag){
            setTv_desc(desc_On);
        }else{
            setTv_desc(desc_Off);
        }
    }


}
