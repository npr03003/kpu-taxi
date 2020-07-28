package kr.ac.kpu.kpu_t

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main_1.*


class Main_Activity1 : AppCompatActivity() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_1)


        //로그인
        btn_Login.setOnClickListener {
            var email:String=Edit_email.text.toString()
            var password:String=Edit_PS.text.toString()
            progressbar.visibility=View.VISIBLE
            mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){task->
                    if(task.isSuccessful){
                        progressbar.visibility=View.GONE
                        Toast.makeText(applicationContext,"성공적으로 가입되었습니다.",Toast.LENGTH_LONG).show()
                        //로그인 성공시 바로 taximain으로 넘어감
                        var intent=Intent(this,TaxiMain::class.java)
                        startActivity(intent)
                    }
                    else{
                        progressbar.visibility=View.GONE
                        Toast.makeText(applicationContext,"다시시도해주시기 바랍니다.",Toast.LENGTH_LONG).show()
                    }
                }
        }


        //회원가입
        btn_Sign.setOnClickListener {
          var intent=Intent(this,SignInActivity::class.java)
            startActivity(intent)
        }



        //아이디 / 비밀번호찾기
        textview_btn_idpassword.setOnClickListener{
            var intent=Intent(this,findpasswordAndID::class.java)
            startActivity(intent)
        }









    }



}
