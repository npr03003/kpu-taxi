package kr.ac.kpu.kpu_t

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        btn_finish.setOnClickListener {
            var email=Sign_edit_email.text.toString()
            var password=Sign_edit_password.text.toString()
            progressbar2.visibility= View.VISIBLE

            mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){task->
                    if(task.isSuccessful){
                        progressbar2.visibility=View.GONE
                        Toast.makeText(applicationContext,"성공적으로 가입되었습니다.", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    else{
                        progressbar2.visibility=View.GONE
                        Toast.makeText(applicationContext,"다시시도해주시기 바랍니다.", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
