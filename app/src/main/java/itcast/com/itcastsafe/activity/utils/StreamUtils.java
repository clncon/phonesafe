package itcast.com.itcastsafe.activity.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author:MingKong
 * @Description:
 * @Date:Created in 23:09 2018/10/23
 * @Modified By:
 */


public class StreamUtils {


    /**
     *读取流中的数据，并且以字符串形式返回
     * @param is
     * @return
     * @throws IOException
     */
     public static String readFromStream(InputStream is) throws IOException {
         ByteArrayOutputStream bos = new ByteArrayOutputStream();
         int len=0;
         byte[] buff = new byte[1024];
         while((len=is.read(buff))!=-1){
             bos.write(buff,0,len);
         }
         bos.flush();
         String result = bos.toString();
         is.close();
         bos.close();
         return result;
     }
}
