package kr.ac.kpu.kpu_t

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_bbody.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class bbodyActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val boardRef = database.getReference("board")
    val userRef = database.getReference("user")
    val user = FirebaseAuth.getInstance()
    var replylist = ArrayList<reply>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bbody)
        val getid = intent
        val id = getid.extras?.getString("id")
        if (id != null) {
            boardRef.child(id).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(i in snapshot.children){
                        if(i.key.equals("reply")){
                            viewreply(id)
                        } else if(i.key.equals("title")){
                            boardtitleTV.text = i.value.toString()
                        } else if(i.key.equals("start")){
                            bdstartTV.text = "출발: "+i.value.toString()
                        } else if(i.key.equals("end")){
                            bdendTV.text = "도착: "+i.value.toString()
                        } else if(i.key.equals("body")){
                            boardbodyTV.text = i.value.toString()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
        replysavebtn.setOnClickListener {
            val c = Calendar.getInstance()
            val date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val time = date.format(c.time)
            if (id != null) {
                boardRef.child(id).addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val postVal : HashMap<String, Any> = HashMap()
                        postVal["text"]=replyET.text.toString()
                        userRef.child(user.uid.toString()).child("name").addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val name=snapshot.value.toString()
                                postVal["name"]=name
                                boardRef.child(id).child("reply").child(time).setValue(postVal)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                                Log.d("reply save","fail")
                            }
                        })
                        replyET.text.clear()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }

    }
    private fun viewreply(boardid:String){


    }
}
class reply(var name:String, var body:String)

class replyadapter(/*val context*/val replylist:ArrayList<reply>)
