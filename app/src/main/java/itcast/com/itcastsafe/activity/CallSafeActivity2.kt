package itcast.com.itcastsafe.activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import itcast.com.itcastsafe.R
import itcast.com.itcastsafe.activity.adapter.MyBaseAdapter
import itcast.com.itcastsafe.activity.bean.BlackNumberInfo
import itcast.com.itcastsafe.activity.dao.BlackNumberDao
import itcast.com.itcastsafe.activity.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_callsafe2.*

class CallSafeActivity2 : AppCompatActivity() {
    var  list_view: ListView?=null
    var currentPageNumber:Int =0
    val pageSize = 10
    var totalPage =0
    var blackNumberInfos:MutableList<BlackNumberInfo>?=null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_callsafe2)

       initUI();
       initData();
    }

    val handler: Handler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            ll_loading.visibility=View.INVISIBLE;
            tv_page_number.text="${currentPageNumber+1}/${totalPage}"
            list_view?.adapter= CallSafeAdapter(blackNumberInfos, this@CallSafeActivity2);

        }
    }
    private fun initData(){

        Thread(){
          kotlin.run {

              var dao = BlackNumberDao(this@CallSafeActivity2);
              totalPage = dao.findTotalSize()/pageSize
              blackNumberInfos= dao.findPar(currentPageNumber,pageSize)
              handler.sendEmptyMessage(0)

          }
        }.start()

        //println(blackNumberInfos.size)




    }
    private fun initUI(){
        //展示加载中。。。
        ll_loading.visibility=View.VISIBLE
        list_view= list_black_view as ListView?
    }

       class CallSafeAdapter: MyBaseAdapter<BlackNumberInfo>{

           constructor(list: MutableList<BlackNumberInfo>?, ctx:Context):super(list!!,ctx)

         override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            var viewHolder: ViewHolder?=null;
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


            return view!!;
        }



    }

    fun prePage(view:View){
        if(currentPageNumber<=0) {
            ToastUtil.showToast(this@CallSafeActivity2,"已经是第一页了")
            return;
        }
        currentPageNumber--;
        initData()


    }

    fun nextPage(view:View){
        if(currentPageNumber>=totalPage-1){
            ToastUtil.showToast(this@CallSafeActivity2,"已经是最后一页了")
            return
        }
        currentPageNumber++
        initData()
    }

    fun jumpPage(view:View){
       if(et_page_number.text.isBlank()){
           ToastUtil.showToast(this@CallSafeActivity2,"页数必须输入")
           return
       }
       val number = et_page_number.text.toString().toInt()
        if(number<1||number>totalPage){
            ToastUtil.showToast(this@CallSafeActivity2,"页数必须在合理范围之内")
            return
        }
        currentPageNumber=number-1
        initData()
    }

    class ViewHolder(var viewItem:View){
         var tv_number: TextView = viewItem.findViewById(R.id.tv_number)
         var tv_mode: TextView = viewItem.findViewById(R.id.tv_mode)

    }

    /**
     * 获取总记录数
     */
    fun getTotalSize():Int{
        var dao = BlackNumberDao(this@CallSafeActivity2)
        return dao.findTotalSize();
    }


}
