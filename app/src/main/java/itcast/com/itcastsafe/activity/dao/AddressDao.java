package itcast.com.itcastsafe.activity.dao;

import android.database.sqlite.SQLiteDatabase;

/**
 * @Author:MingKong
 * @Description:归属地查询工具
 * @Date:Created in 23:44 2018/11/8
 * @Modified By:
 */
public class AddressDao {

     private static  final String PATH = "data/data/itcast.com.itcastsafe/files/address.db";//注意该路径必须是data/data目录中的文件
     public static String getAddressByNumber(String phoneNumber){

         SQLiteDatabase database = SQLiteDatabase.openDatabase(AddressDao.PATH,null,SQLiteDatabase.OPEN_READONLY);
         database.beginTransaction();

         return null;
     }
}
