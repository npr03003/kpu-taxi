package kr.ac.kpu.kpu_t


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_chatting.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton

class Chatting : Fragment() {
    val database = FirebaseDatabase.getInstance()
    val chatRef = database.getReference("chat")
    var chatList = arrayListOf<ChatRoom>()
    val userRef = database.getReference("user")
    val user = FirebaseAuth.getInstance()
    val uid = user.uid.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chatting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val intent = Intent(activity, TaxiRoomSetting::class.java)
        plusFab.setOnClickListener { startActivity(intent) }
        listView.setOnItemClickListener { adapterView, view, i, l ->
            var cKey = chatList.get(i).key
            var cCount = chatList.get(i).count
            userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var chatkey :String
                    chatkey=snapshot.child("chatkey").value as String
                    if(chatkey==cKey)
                    {
                        val cintent = Intent(activity, ChattingRoom::class.java)
                        cintent.putExtra("key",cKey)
                        startActivity(cintent)
                    }
                    else{
                        /*alert("채팅방이 개설되었습니다."){
                            yesButton { finish() }
                        }.show()*/
                        var dialog = activity?.let{AlertDialog.Builder(it)}
                        dialog!!.setMessage("채팅방에 입장하시겠습니까?")
                        dialog.setPositiveButton("입장",DialogInterface.OnClickListener{
                            dialog, which ->
                            userRef.child(uid).child("chatkey").setValue(cKey)
                            chatRef.child(cKey).child("count").setValue(cCount+1)
                            val cintent = Intent(activity, ChattingRoom::class.java)
                            cintent.putExtra("key", cKey)
                            startActivity(cintent)
                        })
                        dialog.setNegativeButton("취소",DialogInterface.OnClickListener{dialog,
                        which-> null})
                        dialog.show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
    override fun onStart() {//fragment 생명주기 onStart
        super.onStart()
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var chatkey :String
                chatkey=snapshot.child("chatkey").value as String
                chatRef.addListenerForSingleValueEvent(object :ValueEventListener{
                    @SuppressLint("RestrictedApi")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(x in snapshot.children){
                            if (x.key.toString()==chatkey){
                                plusFab.visibility = View.INVISIBLE
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        val chatAdapter = context?.let { ChatRoomAdapter(it, chatList) }
        chatRef.addListenerForSingleValueEvent(object :ValueEventListener{//데이터 불러오는
            override fun onDataChange(snapshot: DataSnapshot)  {
            chatList.clear()//arraylist 초기화
            var k : String=""
            var c : Int = 0
            var e : String =""
            var m : Int = 0
            var s : String =""
            var t : String =""
            for(x in snapshot.children){
                    chatRef.child(x.key.toString()).addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            k = x.key.toString()
                            for(y in snapshot.children) {
                                if (y.key.equals("count")) {
                                    c = y.value.toString().toInt()
                                } else if (y.key.equals("end")) {
                                    e = y.value.toString()
                                } else if (y.key.equals("max")) {
                                    m = y.value.toString().toInt()
                                } else if (y.key.equals("member")) {

                                } else if (y.key.equals("start")) {
                                    s = y.value.toString()
                                } else if (y.key.equals("title")) {
                                    t = y.value.toString()
                                }
                            }
                            chatList.add(ChatRoom(k, t, s, e, m, c))
                            listView.adapter = chatAdapter
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}
class ChatRoom(var key : String, var title : String,var start : String,var end : String,var max : Int,var count : Int)

class ChatRoomAdapter (val context: Context, val chatList : ArrayList<ChatRoom>) : BaseAdapter() {
    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_room,null)

        val textTitle = view.findViewById<TextView>(R.id.text1)
        val textpath = view.findViewById<TextView>(R.id.text2)
        val twoImg = view.findViewById<ImageView>(R.id.twoImg)
        val threeImg = view.findViewById<ImageView>(R.id.threeImg)
        val fourImg = view.findViewById<ImageView>(R.id.fourImg)
        val ChatRoom = chatList[position]
        textTitle.text = ChatRoom.title
        textpath.text= "경로 : "+ChatRoom.start + " -> "+ChatRoom.end
        if(ChatRoom.max==4){
            threeImg.visibility = View.VISIBLE
            fourImg.visibility = View.VISIBLE
            if(ChatRoom.count==4){
                fourImg.setImageResource(R.drawable.ic_person_black2_24dp)
                threeImg.setImageResource(R.drawable.ic_person_black2_24dp)
                twoImg.setImageResource(R.drawable.ic_person_black2_24dp)
            }
            else if(ChatRoom.count==3){
                twoImg.setImageResource(R.drawable.ic_person_black2_24dp)
                threeImg.setImageResource(R.drawable.ic_person_black2_24dp)
            }
            else if(ChatRoom.count==2){
                twoImg.setImageResource(R.drawable.ic_person_black2_24dp)
            }
        }
        else if(ChatRoom.max==3){
            threeImg.visibility = View.VISIBLE
            if(ChatRoom.count==3){
                twoImg.setImageResource(R.drawable.ic_person_black2_24dp)
                threeImg.setImageResource(R.drawable.ic_person_black2_24dp)
            }
            else if(ChatRoom.count==2){
                twoImg.setImageResource(R.drawable.ic_person_black2_24dp)
            }
        }
        else if(ChatRoom.max==2){
            if(ChatRoom.count==2){
                twoImg.setImageResource(R.drawable.ic_person_black2_24dp)
            }
        }
            return  view
    }

    override fun getItem(position: Int): Any {
        return chatList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return  chatList.size
    }
}
