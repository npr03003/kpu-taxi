package kr.ac.kpu.kpu_t


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_taxi_main.*
import kotlin.system.exitProcess


class TaxiMain : AppCompatActivity() {
    var backKeyPressedTime : Long = 0
    var chatlist = arrayListOf<ChatRoom>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taxi_main)
        setTitle("KPU-Taxi")



        val fragmentAdapter=PagerAdapter(supportFragmentManager)
        viewpager_main.adapter=fragmentAdapter

        //2020.08.05 남준이가 수정함
        tabs_main.setupWithViewPager(viewpager_main)
        val images = ArrayList<Int>()
        images.add(R.drawable.ic_chat_bubble_orange_24dp)
        images.add(R.drawable.ic_directions_car_orange_24dp)
        images.add(R.drawable.ic_home_orange_24dp)
        for (i in 0..2) tabs_main.getTabAt(i)!!.setIcon(images[i])


    }

    override fun onBackPressed() {
    //1번째 백버튼 클릭
    val auto = intent.getBooleanExtra("auto",false)
    if(!auto){
        finish()
    } else if(System.currentTimeMillis()>backKeyPressedTime+2000){
        backKeyPressedTime = System.currentTimeMillis();
        Toast.makeText(this, "한번 더 누르시면 앱이 종료됩니다", Toast.LENGTH_SHORT).show();
    }
    //2번째 백버튼 클릭 (종료)
        else{
        finishAffinity()
        exitProcess(0)
        }
    }
}