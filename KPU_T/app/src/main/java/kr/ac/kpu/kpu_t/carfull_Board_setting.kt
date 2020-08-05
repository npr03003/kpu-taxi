package kr.ac.kpu.kpu_t

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
        val userRef = database.getReference("user")
        val boardRef = database.getReference("board")

        val key = board(tt,start,end,uid,body)

        userRef.child("$uid/name").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                val value = dataSnapshot.value.toString()

                Log.d("board name added","success")
                Log.d("board ID is",value)

                boardRef.child(key).child("name").setValue(value)
            }
            override fun onCancelled(error: DatabaseError){
                Log.d("board name added","failed")
            }
        })


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