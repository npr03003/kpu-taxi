package kr.ac.kpu.kpu_t

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_taxi_room_setting.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton



class TaxiRoomSetting : AppCompatActivity() {
    val realm = Realm.getDefaultInstance()
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

    private fun chatroom(ID:String , title:String , start:String, end:String, max:Int):String{
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("chat")
        val key = myRef.push().key
        val postVal : HashMap<String, Any> = HashMap()
        postVal["member"]
        postVal["max"] = max
        postVal["count"] = 1
        postVal["title"] = title
        postVal["start"] = start
        postVal["end"]= end


        myRef.child(key!!).setValue(postVal)
        
        myRef.child(key).child("member").setValue(ID)

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
        val myRef = FirebaseDatabase.getInstance()
        val user = FirebaseAuth.getInstance()
        val uid = user.uid
        val ID = myRef.getReference("user/$uid").toString()

        val key = chatroom(ID, tt, strt, nd, num)

        myRef.getReference("user/$uid/chatkey").setValue(key)

        alert("채팅방이 개설되었습니다."){
            yesButton { finish() }
        }.show()
    }
}
