package kr.ac.kpu.kpu_t

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_carfull_board_setting.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class carfull_Board_setting : AppCompatActivity() {

    lateinit var start : String
    lateinit var end : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carfull_board_setting)
        setTitle("게시판에 등록")
        insertMode()
    }


    private fun insertcarfullBoardsetting(){
        val tt = cfRoomName.text.toString()

        if(startET.text == null){
            start = "미설정"
        }else{
            start = startET.text.toString()
        }
        if(endET.text == null){
            end = "미설정"
        }else{
            end = endET.text.toString()
        }

        val body = bodyET.text.toString()

        val database = FirebaseDatabase.getInstance()
        val user = FirebaseAuth.getInstance()
        val uid = user.uid.toString()
        val boardRef = database.getReference("board")

        val key = board(tt,start,end,uid,body)


        alert("게시판이 개설되었습니다."){
            yesButton { finish() }
        }.show()
    }
    private fun board(title:String , start:String, end:String, maker: String, body: String) : String{
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("board")
        val key = myRef.push().key
        val postVal : HashMap<String, Any> = HashMap()
        postVal["title"] = title
        postVal["start"] = start
        postVal["end"]= end
        postVal["body"] = body
        postVal["maker"] = maker
        postVal["reply"] = ""


        myRef.child(key!!).setValue(postVal)

        return key
    }

    private fun insertMode(){
        finishfab.setOnClickListener { insertcarfullBoardsetting() }
    }
}