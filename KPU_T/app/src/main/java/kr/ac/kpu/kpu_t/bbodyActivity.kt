package kr.ac.kpu.kpu_t

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_bbody.*
import java.text.SimpleDateFormat
import java.util.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
            replychildlistener(id)
            showboardbody(id)
        }
        replysavebtn.setOnClickListener {
            if (id != null) {
                savereply(id)
            }
        }

    }


    private fun replychildlistener(boardid: String){
        val replyadded = database.getReference("board/$boardid/reply")
        replyadded.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                viewreply(boardid)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    private fun showboardbody(boardid: String){
        val currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        boardRef.child(boardid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children){
                    if(i.key.equals("title")){
                        boardtitleTV.text = i.value.toString()
                    } else if(i.key.equals("start")){
                        bdstartTV.text = "출발: "+i.value.toString()
                    } else if(i.key.equals("end")){
                        bdendTV.text = "도착: "+i.value.toString()
                    } else if(i.key.equals("body")){
                        boardbodyTV.text = i.value.toString()
                    } else if(i.key.equals("maker")){
                        val userid = i.value.toString()
                        if(userid == currentuser){
                            deletebtn.visibility = View.VISIBLE
                            updatebtn.visibility = View.VISIBLE
                        }
                    } else if(i.key.equals("name")){
                        boardnameTV.text = i.value.toString()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    private fun savereply(boardid:String){
        val c = LocalDateTime.now()
        val date = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val time = c.format(date)
        boardRef.child(boardid).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val postVal : HashMap<String, Any> = HashMap()
                postVal["text"]=replyET.text.toString()
                userRef.child(user.uid.toString()).child("name").addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val name=snapshot.value.toString()
                        postVal["name"]=name
                        boardRef.child(boardid).child("reply").child(time).setValue(postVal)
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


    private fun viewreply(boardid:String){
        val replyatd = replyadapter(this, replylist)
        val replyRef = database.getReference("board/$boardid/reply")
        replyRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                replylist.clear()
                for(i in snapshot.children){
                    replyRef.child(i.key.toString()).addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var nm = ""
                            var bdy = ""
                            for(j in snapshot.children){
                                if(j.key.equals("name")){
                                    nm = j.value.toString()
                                } else if(j.key.equals("text")){
                                    bdy = j.value.toString()
                                }
                            }
                            replylist.add(reply(nm,bdy))
                            replyLV.adapter = replyatd
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}

class reply(var name:String, var body:String)

class replyadapter(val context: Context, val replylist:ArrayList<reply>):BaseAdapter(){
    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_reply,null)

        val name = view.findViewById<TextView>(R.id.replynameTV)
        val text = view.findViewById<TextView>(R.id.replybodyTV)
        val replynum = replylist[position]
        name.text = replynum.name
        text.text = replynum.body

        return view
    }

    override fun getCount(): Int {
        return replylist.size
    }

    override fun getItem(p0: Int): Any {
        return replylist[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }
}
