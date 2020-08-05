package kr.ac.kpu.kpu_t


import android.content.Context
import android.content.Intent
import kotlinx.android.synthetic.main.fragment_chatting.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Chatting : Fragment() {
    val database = FirebaseDatabase.getInstance()
    val chatRef = database.getReference("chat")
    var chatList = arrayListOf<ChatRoom>()

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
    }

    override fun onStart() {//fragment 생명주기 onStart
        super.onStart()
        val chatAdapter = context?.let { ChatRoomAdapter(it, chatList) }
        chatRef.addListenerForSingleValueEvent(object :ValueEventListener{//데이터 불러오는 함수
            override fun onDataChange(snapshot: DataSnapshot)  {
                chatList.clear()//arraylist 초기화
                for(x in snapshot.children){
                    chatRef.child(x.key.toString()).addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var c : Int = 0
                            var e : String =""
                            var m : Int = 0
                            var s : String =""
                            var t : String =""
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
                            chatList.add(ChatRoom(t, s, e, m, c))
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
class ChatRoom(var title : String,var start : String,var end : String,var max : Int,var count : Int)

class ChatRoomAdapter (val context: Context, val chatList : ArrayList<ChatRoom>) : BaseAdapter() {
    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_room,null)

        val textTitle = view.findViewById<TextView>(R.id.text1)
        val textpath = view.findViewById<TextView>(R.id.text2)
        val threeImg = view.findViewById<ImageView>(R.id.threeImg)
        val fourImg = view.findViewById<ImageView>(R.id.fourImg)

        val ChatRoom = chatList[position]
        textTitle.text = ChatRoom.title
        textpath.text= "경로 : "+ChatRoom.start + " -> "+ChatRoom.end
        if(ChatRoom.max==4){
            threeImg.visibility = View.VISIBLE
            fourImg.visibility = View.VISIBLE
        }
        else if(ChatRoom.max==3){
            threeImg.visibility = View.VISIBLE
        }
        return  view
    }

    override fun getItem(position: Int): Any {
        return chatList[position]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return  chatList.size
    }
}
