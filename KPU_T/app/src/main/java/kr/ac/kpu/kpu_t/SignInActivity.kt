package kr.ac.kpu.kpu_t

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.jetbrains.anko.startActivity
import java.util.regex.Pattern

class SignInActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 9001
    private var firebaseAuth: FirebaseAuth? =null
    private val TAG = "MainActivity"
    private val PASSWORD_PATTERN: Pattern = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        firebaseAuth = FirebaseAuth.getInstance()


        btn_finish.setOnClickListener {
            val email = Sign_edit_email.text.toString()
            val password = Sign_edit_password.text.toString()
            val gender = genderchecked()
            if(gender=="null"){
                Toast.makeText(this,"자신의 성별을 선택하여주세요",Toast.LENGTH_SHORT).show()
            }else{
                if(isValidEmail(email) && isValidPasswd(password)) {
                    createEmail(email, password, gender)
                }
            }
        }
        //가입완료



    }

    private fun isValidEmail(email:String): Boolean {
        return if (email.isEmpty()) {
            // 이메일 공백
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // 이메일 형식 불일치
            Toast.makeText(this,"정확한 메일 주소를 입력하여 주세요", Toast.LENGTH_SHORT).show()
            false
        } else if(email.endsWith("@kpu.ac.kr")){
            true
        } else {
            Toast.makeText(this,"학교 메일을 입력하여주세요.", Toast.LENGTH_SHORT).show()
            false
        }

    }
    private fun genderchecked() : String {

        if (malebtn.isChecked&& !femalebtn.isChecked){
            return "남"
        } else if (!malebtn.isChecked && femalebtn.isChecked){
            return "여"
        } else{
            return "null"
        }
    }

    private fun isValidPasswd(password:String): Boolean {
        return if(password.isEmpty()) {
            // 비밀번호 공백
            false
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            Toast.makeText(this,"5자 이상 15자 이하의 숫자, 영문, 특수문자의 조합만 가능합니다.",Toast.LENGTH_SHORT).show()
            return false;
        } else {
            return true;
        }
    }
    private fun createEmail(email:String, password: String, gender: String){
        firebaseAuth!!.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    val user = firebaseAuth?.currentUser
                    if (user != null) {
                        user.sendEmailVerification().addOnCompleteListener(this) {
                            Toast.makeText(this,"인증 메일 발송",Toast.LENGTH_SHORT).show()
                        }
                        val database = FirebaseDatabase.getInstance()
                        val myRef = database.getReference("user")
                        val id = user.uid

                        val postVal : HashMap<String, Any> = HashMap()
                        postVal["name"] = "null"
                        postVal["chatkey"] = "null"
                        postVal["gender"] = gender
                        postVal["images"] = "null"
                        myRef.child(id).setValue(postVal)

                    }
                }else{
                    Toast.makeText(this,"failed",Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun loginEmail(){
        firebaseAuth!!.signInWithEmailAndPassword(Sign_edit_email.text.toString(),Sign_edit_password.text.toString())
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    val user = firebaseAuth?.currentUser
                    if (user != null) {
                        if(user.isEmailVerified){
                            Toast.makeText(this,"login success",Toast.LENGTH_SHORT).show()
                            startActivity<TaxiMain>()
                        }
                        else{
                            Toast.makeText(this,"fail",Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this,"login fail",Toast.LENGTH_SHORT).show()
                }
            }
    }


}
