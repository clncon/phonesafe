package itcast.com.itcastsafe.activity.adapter

import android.content.Context
import android.widget.BaseAdapter
import itcast.com.itcastsafe.activity.bean.BlackNumberInfo

/**
 *@Author:MingKong
 *@Description:
 *@Date:Created in 23:37 2018/12/5
 *@Modified By:
 */
abstract class  MyBaseAdapter<T>(var list: MutableList<T>, var ctx: Context): BaseAdapter(){



    override fun getItem(position: Int): T {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

}