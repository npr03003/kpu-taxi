package kr.ac.kpu.kpu_t

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_findpw.*

class FindpwActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_findpw)

        btn_sendEmail.setOnClickListener {
            findpassword()
        }
    }

    private fun findpassword(){
        FirebaseAuth.getInstance().sendPasswordResetEmail(find_edit_email_forpassword.text.toString()).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(this,"비밀번호 변경 메일을 전송했습니다.", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
