package kr.ac.kpu.kpu_t

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_carfull_board_body.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
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
        setContentView(R.layout.activity_carfull_board_body)
        val getid = intent
        val id = getid.extras?.getString("id")
        if (id != null) {
            brdid = id
        }
        val replyatd = replyadapter(this,replylist)
        val replychild = database.getReference("board/$brdid/reply")
        val childL = object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val name = snapshot.child("name").value.toString()
                val body = snapshot.child("text").value.toString()
                val maker = snapshot.child("maker").value.toString()
                val time = snapshot.child("time").value.toString()
                val boardid = snapshot.child("boardid").value.toString()
                replylist.add(reply(name,body, maker, time, boardid))
                replyLV.adapter = replyatd
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                if(replylist.size==1) {
                    replylist.forEach {
                        if (it.time == snapshot.child("time").value) {
                            replylist.remove(it)
                        }
                    }
                    replyLV.adapter = replyatd
                } else {
                    viewreply(brdid)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        replychild.addChildEventListener(childL)
    }

    override fun onResume() {
        super.onResume()
        showboardbody(brdid)
        viewreply(brdid)
        replysavebtn.setOnClickListener {
            savereply(brdid)
        }
        boarddeletebtn.setOnClickListener {
            val dlg3 = AlertDialog.Builder(this)
            dlg3.setTitle("게시글을 삭제합니다").setMessage("삭제하시겠습니까?")
            dlg3.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                alert("게시글이 삭제되었습니다.") {
                    yesButton {
                        boardRef.child(brdid).removeValue()
                        finish() }
                }.show()
            })
            dlg3.setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            dlg3.show()
        }
        boardupdatebtn.setOnClickListener {
            val intent = Intent(this,CarfullBoardsetting::class.java)
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
            boardRef.child(boardid).child("reply").addListenerForSingleValueEvent(object : ValueEventListener {
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
        }
        if (user != null) {
            if(user.uid == replynum.maker){
                delete.visibility=View.VISIBLE
            }
        }
        Log.d("list size",count.toString())
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
