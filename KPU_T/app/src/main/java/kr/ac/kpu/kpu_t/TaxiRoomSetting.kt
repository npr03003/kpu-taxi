package kr.ac.kpu.kpu_t

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_taxi_room_setting.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class TaxiRoomSetting : AppCompatActivity() {
    var list_of_start = arrayOf("정왕역","오이도역","한국산업기술대학 정문")
    var list_of_end = arrayOf("한국산업기술대학 정문","정왕역","오이도역")
    lateinit var strt : String
    lateinit var nd : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taxi_room_setting)
        setTitle("채팅방 내용 설정")

        spStart.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,list_of_start)
        spEnd.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,list_of_end)
        spStart.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("출발지를 선택하세요.")
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                strt = list_of_start[position]
            }
        }
        spEnd.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("도착지를 선택하세요.")
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                nd = list_of_end[position]
            }
        }
            insertMode()
    }
    private fun chatroom(title:String , start:String, end:String, max:Int) : String{
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("chat")
        val key = myRef.push().key
        val postVal : HashMap<String, Any> = HashMap()
        postVal["member"] = ""
        postVal["max"] = max
        postVal["count"] = 1
        postVal["title"] = title
        postVal["start"] = start
        postVal["end"]= end
        myRef.child(key!!).setValue(postVal)
        return key
    }

    private fun insertMode(){
        floatingActionButton.setOnClickListener {
            if(number2.isChecked==false&&number3.isChecked==false&&number4.isChecked==false){
                alert ( "동승 인원을 선택해주세요." ){
                    yesButton {  }
                }.show()
            }
            else{ insertTaxiSetting() }}
    }

    private fun insertTaxiSetting(){
        val tt = edRoomName.text.toString()
        var num : Int = 0

        if(number4.isChecked){
            num = 4
        }
        else if(number3.isChecked){
            num = 3
        }
        else if(number2.isChecked){
            num = 2
        }
        val database = FirebaseDatabase.getInstance()
        val user = FirebaseAuth.getInstance()
        val uid = user.uid.toString()
        val chatRef = database.getReference("chat")
        val userRef = database.getReference("user")
        val key = chatroom(tt, strt, nd, num)
        userRef.child(uid).child("chatkey").setValue(key)
        userRef.child("$uid/gender").addListenerForSingleValueEvent(object:ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val value = dataSnapshot.value.toString()
                    Log.d("gender added", "success")
                    Log.d("gender is", value)
                    chatRef.child(key).child("member").child(uid).setValue(value)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("member ID added", "failed")
                }
            })
        alert("채팅방이 개설되었습니다."){
            yesButton { finish() }
        }.show()
    }

}