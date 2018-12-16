package itcast.com.itcastsafe.activity

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.*
import android.text.format.Formatter
import android.view.*
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.*
import com.safframework.log.L
import itcast.com.itcastsafe.R
import itcast.com.itcastsafe.activity.bean.AppInfo
import itcast.com.itcastsafe.activity.engine.AppInfos
import kotlinx.android.synthetic.main.activity_app_manager.*
import kotlinx.android.synthetic.main.item_popup.view.*
import android.content.Intent
import android.net.Uri


class AppManagerActivity : Activity(), View.OnClickListener {
    private  var appInfos:List<AppInfo>?=null
    private var adapter:AppManagerAdapter?=null ;
    private val userAppInfos:MutableList<AppInfo> = arrayListOf()
    private val systemAppInfos:MutableList<AppInfo> = arrayListOf()
    private var popupWindow:PopupWindow?=null
    private var clickAppInfo:AppInfo?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_manager)
        initUI()
        initData()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            //运行
            R.id.ll_start ->{
                L.d("${clickAppInfo!!.apkPackageName}")
                val start_localIntent = this.packageManager.getLaunchIntentForPackage(clickAppInfo!!.apkPackageName)
                this.startActivity(start_localIntent)
                popupWindowDissmiss()
            }
            R.id.ll_share->{
                val share_localIntent = Intent("android.intent.action.SEND")
                share_localIntent.type = "text/plain"
                share_localIntent.putExtra("android.intent.extra.SUBJECT", "f分享")
                share_localIntent.putExtra("android.intent.extra.TEXT",
                        "Hi！推荐您使用软件：" + clickAppInfo!!.apkPackageName + "下载地址:" + "https://play.google.com/store/apps/details?id=" + clickAppInfo!!.apkPackageName)
                this.startActivity(Intent.createChooser(share_localIntent, "分享"))
                popupWindowDissmiss()
            }
            R.id.ll_uninstall ->{
                val uninstall_localIntent = Intent("android.intent.action.DELETE", Uri.parse("package:" + clickAppInfo!!.apkPackageName))
                startActivity(uninstall_localIntent)
                popupWindowDissmiss()
            }
            R.id.ll_detail ->{
                val detail_intent = Intent()
                detail_intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                detail_intent.addCategory(Intent.CATEGORY_DEFAULT)
                detail_intent.data = Uri.parse("package:" + clickAppInfo!!.apkPackageName)
                startActivity(detail_intent)
                popupWindowDissmiss()
            }
        }
    }

    private fun initUI() {
        //获取rom内存运行的剩余空间大小
        val freeSpace = Environment.getDataDirectory().freeSpace

        //获取sd卡的剩余空间
        val sd_freeSpace = Environment.getExternalStorageDirectory().freeSpace

        Formatter.formatFileSize(this,freeSpace)
        tv_memory.text= "运行内存：${Formatter.formatFileSize(this,freeSpace)}"
        tv_sd.text= "sd卡可用：${Formatter.formatFileSize(this,sd_freeSpace)}"


          val list_view:ListView = findViewById(R.id.lisv_view)
        list_view.setOnScrollListener(object:AbsListView.OnScrollListener{
            /**
             * @param view
             * @param firstVisibleItem 第一个可见的条位置
             * @param visibleItemCount 一页可以展示多少个条目
             * @param totalItemCount 总共多少条目
             */
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                popupWindowDissmiss()
                 if(userAppInfos!=null && systemAppInfos!=null){
                     if(firstVisibleItem>=(userAppInfos.size+1)){
                         //系统应用程序
                          tv_app.text="系统程序（${systemAppInfos.size}）个"

                     }else{
                         //用户应用程序
                         tv_app.text="用户程序（${userAppInfos.size}）个"
                     }
                 }
            }


            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                return
            }

        })

        list_view.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
             //获取当前点击的对象
            val item = list_view.getItemAtPosition(i)

            if(item!=null&&item is AppInfo){
                clickAppInfo = item
                L.d("item of apkName:${clickAppInfo!!.apkPackageName}")
                val contentView = View.inflate(this@AppManagerActivity,R.layout.item_popup,null)
                contentView.ll_share.setOnClickListener(this@AppManagerActivity)
                contentView.ll_start.setOnClickListener(this@AppManagerActivity)
                contentView.ll_uninstall.setOnClickListener(this@AppManagerActivity)
                contentView.ll_detail.setOnClickListener(this@AppManagerActivity)
                popupWindowDissmiss()
                 //-2标识包裹内容
                 popupWindow= PopupWindow(contentView,-2,-2)
                   //使用popupWindow必须设置背景，不然没有动画
                  popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                  var location = IntArray(2)
                  //获取view展示到窗体的位置
                  view1.getLocationInWindow(location)
                 L.d("----${location[1]}----")
                  popupWindow!!.showAtLocation(adapterView,Gravity.LEFT + Gravity.TOP,70,location[1])


                  val scaleAnimation = ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
                          Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)

                  scaleAnimation.duration=300
                  contentView.startAnimation(scaleAnimation)

            }


        }


    }

     fun popupWindowDissmiss() {
        if(popupWindow!=null&& popupWindow!!.isShowing){
            popupWindow!!.dismiss()
            popupWindow=null

        }
    }

    val handler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message?) {
            val listView:ListView = findViewById(R.id.lisv_view)
            super.handleMessage(msg)

            if(listView.adapter==null){
                adapter = AppManagerAdapter()
                listView.adapter=adapter
            }else{
                 adapter!!.notifyDataSetChanged()
            }


        }
    }

    class ViewHolder(var viewItem:View){
        val iv_icon: ImageView = viewItem.findViewById(R.id.iv_icon)
        val tv_name: TextView = viewItem.findViewById(R.id.tv_name)
        val tv_size: TextView = viewItem.findViewById(R.id.tv_size)
        val tv_type: TextView = viewItem.findViewById(R.id.tv_type)
    }

    inner class AppManagerAdapter : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            //如果当前position==0标识应用程序
            if(position==0){
                 var textView = TextView(this@AppManagerActivity)
                  textView.text="用戶程序（${userAppInfos.size}）"
                  textView.setTextColor(Color.WHITE)
                  textView.setBackgroundColor(Color.GRAY)
                return textView

            }else if(position==userAppInfos.size+1){
                var textView = TextView(this@AppManagerActivity)
                textView.text="系統程序（${systemAppInfos.size}）"
                textView.setTextColor(Color.WHITE)
                textView.setBackgroundColor(Color.GRAY)
                return textView


            }
            var info:AppInfo?=null
            if(position<userAppInfos.size+1){
                //把多出來的特殊條目剪掉
                info = userAppInfos.get(position-1)
            }else {
                 info=systemAppInfos.get(position-(userAppInfos.size+2))

            }
           var view:View?=null
            var viewHolder:ViewHolder?=null
            if(convertView!=null&&convertView is LinearLayout){
                view=convertView
                viewHolder= view.tag as ViewHolder
            }else {
                view = View.inflate(this@AppManagerActivity,R.layout.item_app_manager,null)
                viewHolder= ViewHolder(view)
                view.tag=viewHolder
            }

            viewHolder.iv_icon.background=info!!.icon
            viewHolder.tv_name.text=info.apkName
            viewHolder.tv_size.text=Formatter.formatFileSize(this@AppManagerActivity, info.apkSize!!)
            if(info.isRom!!){
                viewHolder.tv_type.text="手机内存"
            }else{
                viewHolder.tv_type.text="SD卡内存"

            }


            return view!!
        }

        override fun getItem(position: Int): Any? {
            if(position==0){
                return null
            }else if(position==userAppInfos.size+1){
                return null
            }
            var appInfo:AppInfo?=null

            if(position<userAppInfos.size+1){
                //把多出来的特殊条目减去
                appInfo = userAppInfos.get(position-1)
            }else {
                appInfo = systemAppInfos.get(position-userAppInfos.size-2)
            }
            return appInfo
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
           return userAppInfos.size+systemAppInfos.size+2
        }

    }


    private fun initData(){

        Thread(){
           kotlin.run {
               //get all app that is installed
                appInfos = AppInfos.getAppInfos(this@AppManagerActivity)
                userAppInfos.addAll(appInfos!!.groupBy { it.userApp }.getValue(true));
                systemAppInfos.addAll(appInfos!!.groupBy { it.userApp }.getValue(false));
               handler.sendEmptyMessage(0)

           }
        }.start()
    }


    override fun onDestroy() {
        popupWindowDissmiss()
        super.onDestroy()
    }

}
