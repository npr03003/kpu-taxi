package kr.ac.kpu.kpu_t

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_bbody.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
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
    lateinit var brdtitle :String
    lateinit var brdstart :String
    lateinit var brdend :String
    lateinit var brdbody :String
    lateinit var brdid :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bbody)
        val getid = intent
        val id = getid.extras?.getString("id")
        if (id != null) {
            brdid = id
        }
        viewreply(brdid)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        showboardbody(brdid)
        replysavebtn.setOnClickListener {
            savereply(brdid)
            viewreply(brdid)
        }
        boarddeletebtn.setOnClickListener {
            boardRef.child(brdid).removeValue()
        }
        boardupdatebtn.setOnClickListener {
            val intent = Intent(this,carfull_Board_setting::class.java)
            intent.putExtra("boardid",brdid)
            intent.putExtra("title",brdtitle)
            intent.putExtra("start",brdstart)
            intent.putExtra("end",brdend)
            intent.putExtra("body",brdbody)
            startActivity(intent)
        }
    }

    private fun showboardbody(boardid: String){
        val currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        boardRef.child(boardid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children){
                    if(i.key.equals("title")){
                        boardtitleTV.text = i.value.toString()
                        brdtitle = i.value.toString()
                    } else if(i.key.equals("start")){
                        bdstartTV.text = "출발: "+i.value.toString()
                        brdstart = i.value.toString()
                    } else if(i.key.equals("end")){
                        bdendTV.text = "도착: "+i.value.toString()
                        brdend = i.value.toString()
                    } else if(i.key.equals("body")){
                        boardbodyTV.text = i.value.toString()
                        brdbody = i.value.toString()
                    } else if(i.key.equals("maker")){
                        val userid = i.value.toString()
                        if(userid == currentuser){
                            boarddeletebtn.visibility = View.VISIBLE
                            boardupdatebtn.visibility = View.VISIBLE
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
        if(replyET.text.toString()!="") {
            boardRef.child(boardid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val postVal: HashMap<String, Any> = HashMap()
                    postVal["text"] = replyET.text.toString()
                    userRef.child(user.uid.toString()).child("name")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val name = snapshot.value.toString()
                                postVal["name"] = name
                                postVal["time"] = time
                                postVal["maker"] = user.uid.toString()
                                postVal["boardid"] = boardid
                                boardRef.child(boardid).child("reply").child(time).setValue(postVal)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                                Log.d("reply save", "fail")
                            }
                        })
                    replyET.text.clear()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }else{
            alert("댓글란이 비었습니다"){
                yesButton { }
            }.show()
        }
    }


    private fun viewreply(boardid:String){
        val replyatd = replyadapter(this,replylist)
        val replyRef = database.getReference("board/$boardid/reply")
        replyRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children){
                    replylist.clear()
                    replyRef.child(i.key.toString()).addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var nm = ""
                            var bdy = ""
                            var mkr = ""
                            var time =""
                            var boardid =""
                            for(j in snapshot.children){
                                if(j.key.equals("name")){
                                    nm = j.value.toString()
                                } else if(j.key.equals("text")){
                                    bdy = j.value.toString()
                                } else if(j.key.equals("maker")){
                                    mkr = j.value.toString()
                                } else if(j.key.equals("time")){
                                    time = j.value.toString()
                                } else if(j.key.equals("boardid")){
                                    boardid = j.value.toString()
                                }
                            }
                            replylist.add(reply(nm,bdy,mkr,time,boardid))
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

class reply(var name:String, var body:String, var maker:String, var time: String,var boardid: String)

class replyadapter(val context: Context, val replylist:ArrayList<reply>):BaseAdapter(){
    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.item_reply,null)
        val user = FirebaseAuth.getInstance().currentUser
        val name = view.findViewById<TextView>(R.id.replynameTV)
        val text = view.findViewById<TextView>(R.id.replybodyTV)
        val database = FirebaseDatabase.getInstance()
        val replynum = replylist[position]
        val boardrpyRef = database.getReference("board/${replynum.boardid}/reply")
        name.text = replynum.name
        text.text = replynum.body
        val delete = view.findViewById<Button>(R.id.replydeletebtn)
        delete.setOnClickListener {
            boardrpyRef.child(replynum.time).removeValue()
            name.visibility = View.GONE
            text.visibility = View.GONE
            delete.visibility = View.GONE
        }
        if (user != null) {
            if(user.uid == replynum.maker){
                delete.visibility=View.VISIBLE
            }
        }
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
