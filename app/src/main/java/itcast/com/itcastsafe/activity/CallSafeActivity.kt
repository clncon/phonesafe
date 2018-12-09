package itcast.com.itcastsafe.activity

import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Context
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import itcast.com.itcastsafe.R
import itcast.com.itcastsafe.activity.adapter.MyBaseAdapter
import itcast.com.itcastsafe.activity.bean.BlackNumberInfo
import itcast.com.itcastsafe.activity.dao.BlackNumberDao
import itcast.com.itcastsafe.activity.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_callsafe.*

class CallSafeActivity : AppCompatActivity() {
    var  list_view: ListView?=null
    val maxCount=20
    var startIndex=0
    var totalNumber=0
    var blackNumberInfos:MutableList<BlackNumberInfo>?=null;
    var adapter:CallSafeAdapter?=null
    var dao:BlackNumberDao?=null
    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_callsafe)

       initUI();
       initData();

    }

    val handler: Handler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            ll_loading.visibility=View.INVISIBLE;
            if(list_view!!.adapter==null){
                adapter=CallSafeAdapter(blackNumberInfos,this@CallSafeActivity);
                list_view!!.adapter=adapter
            }else{
               adapter!!.notifyDataSetChanged()
            }


        }
    }
    private fun initData(){
        if(dao==null){
            dao = BlackNumberDao(this@CallSafeActivity);
        }

        //查询总记录数
        totalNumber=getTotalSize()
        Thread(){
          kotlin.run {


              if(blackNumberInfos==null){
                  blackNumberInfos= dao!!.findPar2(startIndex,maxCount)

              }else{
                 blackNumberInfos!!.addAll(dao!!.findPar2(startIndex,maxCount))
              }
              handler.sendEmptyMessage(0)

          }
        }.start()

        //println(blackNumberInfos.size)




    }
    private fun initUI(){
        //展示加载中。。。
        ll_loading.visibility=View.VISIBLE
        list_view= list_black_view as ListView?
        list_view!!.setOnScrollListener(object : AbsListView.OnScrollListener{
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                when(scrollState){
                    AbsListView.OnScrollListener.SCROLL_STATE_IDLE->{
                        //获取最后一条数据
                        if(list_view!!.lastVisiblePosition==blackNumberInfos!!.size-1){
                            startIndex+=maxCount
                            if(startIndex>totalNumber){
                                ToastUtil.showToast(this@CallSafeActivity,"没有更多数据了")
                                return
                            }
                            initData()
                        }

                    }
                }
            }

            /**
             *  //listview滚动的时候调用的方法
            //时时调用。当我们的手指触摸的屏幕的时候就调用
             */
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
               return
            }

        })
    }

       class CallSafeAdapter: MyBaseAdapter<BlackNumberInfo>{

           constructor(list: MutableList<BlackNumberInfo>?, ctx:Context):super(list!!,ctx)

         override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            var viewHolder:ViewHolder?=null;
            var view:View?=null;

            if(convertView==null){
                view = LayoutInflater.from(parent?.context).inflate(R.layout.item_call_safe,null);

                viewHolder = ViewHolder(view)
                view.tag=viewHolder

            }else{
               view=convertView;
               viewHolder= view.tag as ViewHolder?;

            }
            val item:BlackNumberInfo =list.get(position);
            viewHolder!!.tv_number.text=item.number;
            when(item.mode!!.toInt()){
                1 -> viewHolder.tv_mode.text="电话和短信拦截"
                2 -> viewHolder.tv_mode.text="电话拦截"
                3 -> viewHolder.tv_mode.text="短信拦截"
            }

             /**
              * 删除黑名单
              */
             viewHolder.iv_delete.setOnClickListener(object:View.OnClickListener {
                 override fun onClick(v: View?) {
                    val dao = BlackNumberDao(parent!!.context)

                     val flag:Boolean = dao.delete(item.number.toString())
                     if(flag){
                         ToastUtil.showToast(parent.context,"刪除成功")
                         list.remove(item)
                         notifyDataSetChanged()
                     }else{
                         ToastUtil.showToast(parent.context,"刪除失敗")
                     }
                 }
             })


            return view!!;
        }



    }


    class ViewHolder(var viewItem:View){
         var tv_number: TextView = viewItem.findViewById(R.id.tv_number)
         var tv_mode: TextView = viewItem.findViewById(R.id.tv_mode)
         var iv_delete: ImageView =viewItem.findViewById(R.id.iv_delete)

    }

    /**
     * 获取总记录数
     */
    fun getTotalSize():Int{
        BlackNumberDao(this@CallSafeActivity)
        return dao!!.findTotalSize();
    }

    /**
     * 添加黑名单
     */
    fun addBlackNumber(view:View){

        val builder = AlertDialog.Builder(this@CallSafeActivity)
        val dialog =builder.create()
        val view = View.inflate(this@CallSafeActivity,R.layout.dialog_add_black_number,null)
        val et_number: EditText = view.findViewById(R.id.et_number)
        val cb_phone:CheckBox = view.findViewById(R.id.cb_phone)
        val cb_message:CheckBox = view.findViewById(R.id.cb_message)
        val bt_ok:Button = view.findViewById(R.id.bt_ok)
        val bt_cancel:Button = view.findViewById(R.id.bt_cancel)

         bt_cancel.setOnClickListener(object:View.OnClickListener {
             override fun onClick(v: View?) {
                 dialog.dismiss()
             }
         })

        bt_ok.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                val str_number = et_number.text.toString()
                if(TextUtils.isEmpty(str_number)){
                    ToastUtil.showToast(this@CallSafeActivity,"请填写黑名单号码")
                    return
                }

                var mode:String?=null
                when{
                    cb_phone.isChecked&&cb_message.isChecked -> mode="1"
                    cb_phone.isChecked -> mode="2"
                    cb_message.isChecked -> mode="3"
                    else -> {
                        ToastUtil.showToast(this@CallSafeActivity,"请勾选拦截模式")
                        return
                    }
                }

                var blackNumberInfo = BlackNumberInfo()
                blackNumberInfo.number=str_number
                blackNumberInfo.mode=mode
                blackNumberInfos!!.add(blackNumberInfo)
                //把电话号码和拦截模式添加到数据库
                  dao!!.add(str_number,mode)
                if(adapter==null){
                    adapter=CallSafeAdapter(blackNumberInfos,this@CallSafeActivity)
                    list_view!!.adapter=adapter
                }else adapter!!.notifyDataSetChanged()

                dialog.dismiss()
            }

        })

        dialog.setView(view)
        dialog.show()
    }



}
