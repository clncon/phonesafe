package itcast.com.itcastsafe.activity.dao;

import android.database.Cursor;
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
        String address="未知号码";
         //获取数据库对象
         SQLiteDatabase database = SQLiteDatabase.openDatabase(AddressDao.PATH,null,SQLiteDatabase.OPEN_READONLY);
         //手机号码特点：1+(3,4,5,6,7,8)+(9位数字)
         //正则表达式
     //^1[3-8]\d{9}$

        if(phoneNumber.matches("^1[3-8]\\d{9}$")){
            //匹配手机号
            Cursor cursor = database.rawQuery("select location from data2 where id=(select outkey from data1 where id=?)",new String[]{phoneNumber.substring(0,7)});
            if(cursor.moveToNext()){
                address = cursor.getString(0);
            }
            cursor.close();
        }else if(phoneNumber.matches("^\\d+$")){
            //匹配数字
            switch (phoneNumber.length()){

                case 3:
                    address="报警电话";
                    break;
                case 4:
                    address="模拟器";
                case 5:
                    address="客服电话";
                case 7:
                case 8:
                    address="本地号码";
                 default:
                     if(phoneNumber.startsWith("0")&&phoneNumber.length()>10){
                         //有可能是长途电话
                         //有些区号是4位，有些区位是3号
                         //先查询4位区号
                         Cursor cursor = database.rawQuery("select location from data2 where area=?",new String[]{
                                 phoneNumber.substring(1,4)
                         });

                         if(cursor.moveToNext()){
                             address = cursor.getString(0);
                         }else {
                             //查询3位区号
                                    cursor = database.rawQuery("select location from data2 where area=?",new String[]{
                                     phoneNumber.substring(1,3)
                             });

                                    if(cursor.moveToNext()){
                                       address= cursor.getString(0);
                                    }

                                    cursor.close();

                         }

                     }
                     break;
            }
        }

         database.close();//关闭数据库
         return address;
     }
}
