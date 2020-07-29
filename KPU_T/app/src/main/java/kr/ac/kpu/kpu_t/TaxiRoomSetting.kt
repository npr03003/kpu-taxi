package kr.ac.kpu.kpu_t

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
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
    var start : String = "산기대"
    var end : String = "화이팅"


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
                start = list_of_start[position]
            }
        }
        spEnd.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                println("도착지를 선택하세요.")
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                end = list_of_end[position]
            }
        }
        val id = intent.getLongExtra("id",-1L)
        if(id == -1L){
            insertMode()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
    private fun insertMode(){
        floatingActionButton.setOnClickListener { insertTaxiSetting() }
    }


    private fun insertTaxiSetting(){
        realm.beginTransaction()
        val setting = realm.createObject<ChatRoom>(nextId())
        setting.title = edRoomName.text.toString()
        setting.start = start
        setting.end = end
        realm.commitTransaction()

        alert("채팅방이 개설되었습니다."){
            yesButton { finish() }
        }.show()
    }
    private fun nextId(): Int{
        val maxId = realm.where<ChatRoom>().max("id")
        if (maxId != null){
            return maxId.toInt() +1
        }
        return 0
    }

}
