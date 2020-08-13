package kr.ac.kpu.kpu_t


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_taxi_main.*
import kotlin.system.exitProcess


class TaxiMain : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val chatRef = database.getReference("chat")
    val userRef = database.getReference("user")
    val user = FirebaseAuth.getInstance()
    val uid = user.uid.toString()
    var backKeyPressedTime : Long = 0

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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.deleteMenu -> {
                DeleteRoom()
                recreate()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun DeleteRoom(){
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var chatkey :String
                chatkey=snapshot.child("chatkey").value as String
                chatRef.child(chatkey).removeValue()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
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