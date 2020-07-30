package kr.ac.kpu.kpu_t

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_carfull_board_setting.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class carfull_Board_setting : AppCompatActivity() {
     val cfrealm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carfull_board_setting)
        setTitle("게시판에 등록")

        val cfid = intent.getLongExtra("cfid",-1L)
        if(cfid==-1L){
            insertMode()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cfrealm.close()
    }
    private fun insertcarfullBoardsetting(){
        cfrealm.beginTransaction()
        val setting = cfrealm.createObject<carfullBoard>(nextId())
        setting.cftitle = cfRoomName.text.toString()
        if(startET.text == null){
            setting.cfstart = "미설정"
        }else{
            setting.cfstart = startET.text.toString()
        }
        if(endET.text == null){
            setting.cfend = "미설정"
        }else{
            setting.cfend = endET.text.toString()
        }
        cfrealm.commitTransaction()

        alert("게시판이 개설되었습니다."){
            yesButton { finish() }
        }.show()
    }
    private fun nextId(): Int{
        val maxId = cfrealm.where<carfullBoard>().max("cfid")
        if (maxId != null){
            return maxId.toInt() +1
        }
        return 0
    }
    private fun insertMode(){
        finishfab.setOnClickListener { insertcarfullBoardsetting() }
    }
}