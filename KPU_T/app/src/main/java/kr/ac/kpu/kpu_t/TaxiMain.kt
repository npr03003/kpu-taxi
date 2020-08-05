package kr.ac.kpu.kpu_t


import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_taxi_main.*
import kotlinx.android.synthetic.main.fragment_chatting.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.FirebaseDatabase
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.sql.Ref


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
        images.add(R.drawable.ic_home_orange_24dp)
        images.add(R.drawable.ic_directions_car_orange_24dp)
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