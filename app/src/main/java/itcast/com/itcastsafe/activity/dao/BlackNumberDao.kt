package itcast.com.itcastsafe.activity.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.SystemClock
import itcast.com.itcastsafe.activity.bean.BlackNumberInfo
import java.util.*

/**
 *@Author:MingKong
 *@Description:
 *@Date:Created in 21:13 2018/12/2
 *@Modified By:
 */

class BlackNumberDao {
    private var helper: BlackNumberOpenHelper;
    constructor(context: Context) {
        helper = BlackNumberOpenHelper(context);

    }


    /**
     * @description :添加黑名单
     * @param number 黑名单号码
     * @param number 拦截模式
     */
    fun add(number: String, mode: String): Boolean {

        var db: SQLiteDatabase = helper.writableDatabase;
        var contentValues: ContentValues = ContentValues();
        contentValues.put("number", number);
        contentValues.put("mode", mode);
        var rowid: Long = db.insert("blacknumber", null, contentValues);
        return rowid.compareTo(-1) != 0

    }

    /**
     * @description :通过手机号删除
     * @param手机号码
     */
    fun delete(number: String): Boolean {

        var db: SQLiteDatabase = helper.writableDatabase;
        var rowid: Int = db.delete("blacknumber", "number=?", arrayOf(number));
        return rowid != 0

    }

    /**
     * 通过手机号修改拦截模式
     * @param number
     * @param mode
     */
    fun changNumberMode(number: String, mode: String): Boolean {

        var db: SQLiteDatabase = helper.writableDatabase;
        var values: ContentValues = ContentValues();
        values.put("mode", mode);
        var rownumber: Int = db.update("blacknumber", values, "number=?", arrayOf(number));
        return rownumber != 0;
    }

    /**
     * 返回一个黑名单号码拦截的mode
     * @param number
     */
    fun findNumber(number: String): String {
        var mode: String = "";
        var db: SQLiteDatabase = helper.writableDatabase;
        var cursor: Cursor = db.query("blacknumber", arrayOf("mode"),
                "number=?", arrayOf(number), null, null, null);
        if (cursor.moveToNext()) mode = cursor.getString(0);
        cursor.close();
        db.close();
        return mode;
    }


     fun findAll(): List<BlackNumberInfo> {
        var db: SQLiteDatabase = helper.writableDatabase;
        var cursor:Cursor=db.query("blacknumber", arrayOf("number", "mode"),
                null, null, null, null, null);

        var blackNumberInfos:MutableList<BlackNumberInfo> = mutableListOf();
        while (cursor.moveToNext()){
            var blackNumberInfo = BlackNumberInfo();
            blackNumberInfo.mode=cursor.getString(1);
            blackNumberInfo.number=cursor.getString(0);
            blackNumberInfos.add(blackNumberInfo);
        }
        cursor.close()
         db.close()
         SystemClock.sleep(3000)
         return blackNumberInfos
    }
    



}