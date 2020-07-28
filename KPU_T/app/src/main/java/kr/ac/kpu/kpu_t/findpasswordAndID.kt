package kr.ac.kpu.kpu_t

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_findpassword_and_id.*





class findpasswordAndID : AppCompatActivity() {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_findpassword_and_id)



        btn_finish_password.setOnClickListener {
            val email:String=Edit_findpassword.text.toString()
            resetPassword(email)
        }
    }

    private fun resetPassword(email:String){
        mAuth?.sendPasswordResetEmail(email)
            ?.addOnCompleteListener(this){
                if(it.isSuccessful){
                    Toast.makeText(applicationContext,"비밀번호 재설정 성공!",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(applicationContext,"비밀번호 설정 실패!",Toast.LENGTH_LONG).show()
                }
            }
    }



}
