package kr.ac.kpu.kpu_t


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_set_nickname.*
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
        var nickname2:String=""
        var Email:String=""
        val database = FirebaseDatabase.getInstance()
        val Ref = database.getReference("user")
        var isgranted:Int=0

        Ref.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                for(x in snapshot.children){

                    Ref.child(x.key.toString()).addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for(y in snapshot.children) {
                                if(y.key.equals("name")){
                                    nickname2=y.value.toString()

                                    btn_checkoverlap.setOnClickListener {
                                        nickname=Edit_nickname.text.toString()
                                        if(nickname.equals(nickname2)){
                                            textview_result_cant.visibility= View.VISIBLE
                                            //중복되었을 떄
                                        }
                                        else{
                                            Edit_nickname.text.toString()
                                            textview_result_can.visibility=View.VISIBLE
                                            isgranted=1
                                            Ref.child(uid).child("name").setValue(nickname)
                                            //중복 안되고 닉넴 괜찮을 때

                                        }

                                    }

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

            }


        })



        btn_finish_setnickname.setOnClickListener {
            if(isgranted==1){

                alert("닉네임이 변경되었습니다."){
                    yesButton { finish() }
                }.show()

            }
            else{
                Toast.makeText(applicationContext,"중복을 확인하여 주십시오.",Toast.LENGTH_LONG).show()
            }
        }




    }
}
