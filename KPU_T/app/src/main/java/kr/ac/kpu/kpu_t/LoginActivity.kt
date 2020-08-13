package kr.ac.kpu.kpu_t

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import java.util.regex.Pattern
import kotlin.system.exitProcess


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var TAG = "LoginActivity"
    var backKeyPressedTime: Long = 0
    private val PASSWORD_PATTERN: Pattern = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$")
    private val RC_SIGN_IN = 9001

    private fun saveData(auto: Boolean, save: Boolean, id:String) {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()

        editor.putString("KEY_ID",id).apply()
        editor.putBoolean("KEY_AUTO", auto)
            .apply()
        editor.putBoolean("KEY_SAVE", save)
            .apply()
    }

    private fun loadData() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val auto = pref.getBoolean("KEY_AUTO", false)
        val save = pref.getBoolean("KEY_SAVE",false)
        val id = pref.getString("KEY_ID","")
        if (auto) {
            autoLogin.isChecked = true
        }
        if(save){
            saveid.isChecked=true
            Edit_email.setText(id)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        loadData()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null && autoLogin.isChecked) {
            startActivity<MainActivity>("auto" to autoLogin.isChecked)
        } else {

            // No user is signed in
            btn_Login.setOnClickListener {
                Login()
                progressbar.visibility = View.VISIBLE
            }
            btn_Sign.setOnClickListener {
                startActivity<SignInActivity>()
                finish()
            } //회원가입
            textview_btn_idpassword.setOnClickListener {
                startActivity<FindpwActivity>()
            } //비밀번호 찾기
        }


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        //updateUI(currentUser)
    }

    fun Login() {
        val email = Edit_email.text.toString()
        val password = Edit_PS.text.toString()
        val auto = autoLogin.isChecked
        val save = saveid.isChecked
        if (email.length < 1 || password.length < 1) {
            val toast = Toast.makeText(this, "입력칸이 공란입니다.", Toast.LENGTH_SHORT)
            toast.show()
        } else {
            saveData(auto,save,email)
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = FirebaseAuth.getInstance().currentUser
                        //updateUI(user)
                        if (user != null) {
                            if (user.isEmailVerified) {
                                //sharedpreference
                                progressbar.visibility = View.GONE
                                val sharedPref = getSharedPreferences("shared",Context.MODE_PRIVATE)
                                with (sharedPref.edit()) {
                                    putString("email",email)
                                    commit()
                                }
                                startActivity<MainActivity>("auto" to autoLogin.isChecked)
                            } else {
                                Toast.makeText(this, "메일을 인증하여 주세요.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this, "존재하지 않는 계정입니다.", Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }
                }
        }
    }

    override fun onBackPressed() {
        //1번째 백버튼 클릭
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "한번 더 누르시면 앱이 종료됩니다", Toast.LENGTH_SHORT).show();
        }
        //2번째 백버튼 클릭 (종료)
        else {
            finishAffinity()
            exitProcess(0)
        }
    }


}


