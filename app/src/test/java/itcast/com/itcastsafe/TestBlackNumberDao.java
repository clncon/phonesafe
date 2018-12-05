package itcast.com.itcastsafe;

import android.content.Context;
import android.test.AndroidTestCase;
import itcast.com.itcastsafe.activity.dao.BlackNumberDao;

import java.util.Random;

/**
 * @Author:MingKong
 * @Description:
 * @Date:Created in 19:50 2018/12/5
 * @Modified By:
 */
public class TestBlackNumberDao extends AndroidTestCase {

    public Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context=getContext();
    }

    public void testAdd(){
        BlackNumberDao dao = new BlackNumberDao(context);
        Random random = new Random();
        for (int i=0;i<200;i++){
            Long number = 13300000000l +i;
            dao.add(number+"",String.valueOf(random.nextInt(3)+1));
        }
    }
}
