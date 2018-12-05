package itcast.com.itcastsafe.activity.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 *@Author:MingKong
 *@Description:
 *@Date:Created in 20:58 2018/12/2
 *@Modified By:
 */
 class BlackNumberOpenHelper: SQLiteOpenHelper {

     /*次级构造函数*/
    constructor(context:Context):super(context,"safe.db",null,1){
      /*基类构造函数*/
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table blacknumber (_id integer primary key autoincrement,number varchar(20),mode varchar(2))");
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}