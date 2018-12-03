package itcast.com.itcastsafe.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import itcast.com.itcastsafe.R
import kotlinx.android.synthetic.main.activity_callsafe.*

class CallSafeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_callsafe)

       initUI();
    }

    private fun initUI(){
      list_black_view

    }
}
