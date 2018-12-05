package itcast.com.itcastsafe

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import itcast.com.itcastsafe.activity.dao.BlackNumberDao
import org.junit.Test
import org.junit.runner.RunWith

import java.util.Random

import org.junit.Assert.*

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
//@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()

        assertEquals("itcast.com.itcastsafe", appContext.packageName)
    }

    @Test
    fun testAdd() {
        val dao = BlackNumberDao(InstrumentationRegistry.getTargetContext())
        val random = Random()
        for (i in 0..199) {
            val number = 13300000000L + i
            dao.add(number.toString() + "", (random.nextInt(3) + 1).toString())
        }

    }


    @Test
    fun testFind(){
        val dao:BlackNumberDao = BlackNumberDao(InstrumentationRegistry.getTargetContext())
        val number = dao.findNumber("13300000000")


   /* fun testDelete() {
        val dao = BlackNumberDao(InstrumentationRegistry.getTargetContext())
        val delete = dao.delete("13300000000")
        assertEquals(true,delete)
    }*/




    }


    @Test
    fun testFindAll(){
        val dao = BlackNumberDao(InstrumentationRegistry.getTargetContext())
        val list = dao.findAll()
        for(info in list){
            println(info.mode+":"+info.number)
        }
    }
}
