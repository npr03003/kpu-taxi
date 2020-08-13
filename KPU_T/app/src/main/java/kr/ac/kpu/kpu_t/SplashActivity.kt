package kr.ac.kpu.kpu_t

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.startActivity


class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        val handler = Handler()
        handler.postDelayed({
            startActivity<LoginActivity>()
            //뒤로가기 했을 때 다시 안나오게 >>finish!!
            finish()
        }, 2000)
    }
}
