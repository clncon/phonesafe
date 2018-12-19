package itcast.com.itcastsafe.activity.utils

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.os.SystemClock
import android.util.Xml
import android.widget.ProgressBar
import com.safframework.log.L
import org.xmlpull.v1.XmlSerializer
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

/**
 *@Author:MingKong
 *@Description:短信备份工具类
 *@Date:Created in 22:04 2018/12/19
 *@Modified By:
 */
object SmsUtils {

    /**
     * 备份短信的接口
     */
    interface BackUpSms{
        fun before(count:Int)
        fun onBackUpSms(progress:Int)


    }

    fun backup(context: Context, callBack:BackUpSms):Boolean{

         var flag=true;
        try {

            //1.判断当前用户的手机上面是否有sd卡
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                val file = File(Environment.getExternalStorageDirectory(),"backup.xml")
                val os = FileOutputStream(file)
                val serializer = Xml.newSerializer()
                //序列化到sd卡然后设置编码方式
                serializer.setOutput(os,"utf-8")
                //standalone标识当前的xml是否是独立的
                serializer.startDocument("utf-8",true)
                serializer.startTag(null,"smss")

                //2.如果有SD卡
                val uri= Uri.parse("content://sms/")
                val cursor =context.contentResolver.query(uri, arrayOf("address","date","type","body"),null,null,null)

                 val count = cursor.count
                serializer.attribute(null,"size",count.toString())
                /*  progressDialog.max=count
                  progressBar.max=count*/
                callBack.before(count)
                var process=0
                while(cursor.moveToNext()){
                    serializer.startTag(null,"sms")
                    serializer.startTag(null,"address")
                    serializer.text(cursor.getString(0))
                    serializer.endTag(null,"address")
                    serializer.startTag(null,"date")
                    serializer.text(cursor.getString(1))
                    serializer.endTag(null,"date")
                    serializer.startTag(null,"type")
                    serializer.text(cursor.getString(2))
                    serializer.endTag(null,"type")
                    serializer.startTag(null,"body")
                    serializer.text(cursor.getString(3))
                    serializer.endTag(null,"body")
                    serializer.endTag(null,"sms")
                    process++
                  /*  progressDialog.progress=process
                    progressBar.progress=process*/
                    callBack.onBackUpSms(process)
                    SystemClock.sleep(200)
                }
                serializer.endTag(null,"smss")
                serializer.endDocument()

                //3.写短信（写到sd卡）



            }

        }catch (e:Exception){
            flag=false


        }


        return flag
     }
}