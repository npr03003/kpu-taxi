package kr.ac.kpu.kpu_t


import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_set_nickname.*
import kotlinx.android.synthetic.main.fragment_chatting.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton


class SetNickname : AppCompatActivity() {

    private val TAG : String? = MainActivity :: class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_nickname)


        val user = FirebaseAuth.getInstance()
        val uid = user.uid.toString()
        var nickname:String=""
        var Email:String=""
        val database = FirebaseDatabase.getInstance()
        val Ref = database.getReference("user")
        var isgranted:Int=1



        btn_checkoverlap.setOnClickListener{
            isgranted=1
            nickname=Edit_nickname.text.toString()
            Ref.addListenerForSingleValueEvent(object :ValueEventListener{//데이터 불러오는
            override fun onDataChange(snapshot: DataSnapshot)  {
                var nickname2:String=""
                var uuid:String=""
                for(x in snapshot.children){
                    Ref.child(x.key.toString()).addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            uuid = x.key.toString()
                            for(y in snapshot.children) {
                                if (y.key.equals("name")) {
                                    nickname2 = y.value.toString()
                                    if(nickname2.equals(nickname)){
                                        isgranted=0
                                        textview_result_can.visibility=View.GONE
                                        textview_result_cant.visibility=View.VISIBLE
                                        break

                                    }
                                    else{
                                        textview_result_can.visibility=View.VISIBLE
                                        textview_result_cant.visibility=View.GONE
                                    }

                                }
                                if(isgranted==0){
                                    break
                                }
                            }

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

        btn_finish_setnickname.setOnClickListener {
            if(isgranted==0){
                Toast.makeText(applicationContext,"중복을 확인하여 주십시오.",Toast.LENGTH_SHORT).show()
            }
            else{
                val dlg3 = AlertDialog.Builder(this)
                dlg3!!.setTitle("닉네임 설정").setMessage("닉네임을 한번 바꾸면 다시 설정할 수 없습니다. 계속하시겠습니까?")
                dlg3.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                    Ref.child(uid).child("name").setValue(nickname)
                    Ref.child(uid).child("nickset").setValue("complete")

                    alert("닉네임이 설정되었습니다."){
                        yesButton { finish() }
                    }.show()

                })
                dlg3.setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                dlg3.show()


            }
        }




    }



}
