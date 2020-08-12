package kr.ac.kpu.kpu_t


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chatting_room.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChattingRoom : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: MyAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    val database = FirebaseDatabase.getInstance()
    val chatRef = database.getReference("chat")
    val userRef = database.getReference("user")
    val user = FirebaseAuth.getInstance()
    val uid = user.uid.toString()
    var messageList = arrayListOf<Chat>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting_room)
        setTitle("채팅")
        var actionBar = actionBar
        actionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }
        var intent = getIntent()
        var key = intent.extras?.getString("key")
        ChatBordSet(key.toString())
        ChatUpdate(key.toString())
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var name :String
                name=snapshot.child("name").value as String
                viewManager = LinearLayoutManager(this@ChattingRoom)
                viewAdapter = MyAdapter(messageList,name)
                recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
                    // use this setting to improve performance if you know that changes
                    // in content do not change the layout size of the RecyclerView
                    setHasFixedSize(true)
                    // use a linear layout manager
                    layoutManager = viewManager

                    // specify an viewAdapter (see also next example)
                    adapter = viewAdapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        btnSend.setOnTouchListener { _: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    btnSend.setBackgroundColor(Color.BLUE)
                    ChatAdd(key.toString())
                }
                MotionEvent.ACTION_UP -> {
                    btnSend.setBackgroundColor(Color.parseColor("#ffffA500"))
                    editSend.setText("")
                }
            }
            true
        }
    }
    fun ChatBordSet(key: String){
        chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            //데이터 불러오는
            override fun onDataChange(snapshot: DataSnapshot) {
                var c: Int = 0
                var e: String = ""
                var m: Int = 0
                var s: String = ""
                var t: String = ""
                for (x in snapshot.children) {
                    if (x.key.equals(key)) {
                        chatRef.child(x.key.toString()).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (y in snapshot.children) {
                                    if (y.key.equals("count")) {
                                        c = y.value.toString().toInt()
                                    } else if (y.key.equals("end")) {
                                        e = y.value.toString()
                                    } else if (y.key.equals("max")) {
                                        m = y.value.toString().toInt()
                                    } else if (y.key.equals("start")) {
                                        s = y.value.toString()
                                    } else if (y.key.equals("title")) {
                                        t = y.value.toString()
                                    }
                                }
                                roomText1.setText(t)
                                roomText2.setText("경로 : " + s + "->" + e)
                                if (m == 4) {
                                    roomThreeImg.visibility = View.VISIBLE
                                    roomFourImg.visibility = View.VISIBLE
                                } else if (m == 3) {
                                    roomThreeImg.visibility = View.VISIBLE
                                }
                                if (c==4){
                                    roomFourImg.setImageResource(R.drawable.ic_person_black2_24dp)
                                    roomTwoImg.setImageResource(R.drawable.ic_person_black2_24dp)
                                    roomThreeImg.setImageResource(R.drawable.ic_person_black2_24dp)
                                }
                                else if(c==3){
                                    roomTwoImg.setImageResource(R.drawable.ic_person_black2_24dp)
                                    roomThreeImg.setImageResource(R.drawable.ic_person_black2_24dp)
                                }
                                else if(c==2){
                                    roomTwoImg.setImageResource(R.drawable.ic_person_black2_24dp)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
    fun ChatUpdate(key: String) {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // A new comment has been added, add it to the displayed list
                var name: String = dataSnapshot.child("name").value.toString()
                var message: String = dataSnapshot.child("text").value.toString()
                messageList.add(Chat(message, name))
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    applicationContext, "Failed to load comments.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        chatRef.child(key.toString()).child("member").addChildEventListener(childEventListener)
    }

    fun ChatAdd(key: String) {
        val c = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val dateformat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val datetime = c.format(dateformat)
        /*val c = Calendar.getInstance()
        val dateformat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val datetime = dateformat.format(c.time)*/
        var message = editSend.text.toString()
        val TimeRef = chatRef.child(key.toString()).child("member").child(datetime.toString())//시간을 id로 사용
        userRef.child(uid).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var name :String
                for(x in snapshot.children){
                    if(x.key.equals("name")){
                        name = x.value.toString()
                        TimeRef.child("name").setValue(name)//닉네임
                        messageList.clear()
                        ChatUpdate(key.toString())
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        TimeRef.child("text").setValue(message)//메세지
        Handler().postDelayed(Runnable {
            run {
                my_recycler_view.scrollToPosition(viewAdapter.itemCount-1)
            }
        },50)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chatmenu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.exitMenu -> {
                var dialog = AlertDialog.Builder(this)
                dialog!!.setMessage("채팅방을 나가시겠습니까?")
                dialog.setPositiveButton("나가기", DialogInterface.OnClickListener{
                        dialog, which ->
                    var cintent = getIntent()
                    var key = cintent.extras?.getString("key")
                    userRef.child(uid).child("chatkey").setValue("null")
                    chatRef.child(key.toString()).addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var c: Int
                            for (y in snapshot.children) {
                                if (y.key.equals("count")) {
                                    c = y.value.toString().toInt()
                                    chatRef.child(key.toString()).child("count").setValue(c-1)
                                }
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    })
                    val intent = Intent(this,TaxiMain::class.java)
                    startActivity(intent)
                })
                dialog.setNegativeButton("취소", DialogInterface.OnClickListener{ dialog,
                                                                                which-> null})
                dialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
class Chat(var message : String, var Nick : String)