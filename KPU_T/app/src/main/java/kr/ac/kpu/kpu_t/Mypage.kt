package kr.ac.kpu.kpu_t


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import org.jetbrains.anko.startActivity
import kotlinx.android.synthetic.main.fragment_mypage.*
import androidx.fragment.app.Fragment

/**
 * A simple [Fragment] subclass.
 */
class Mypage : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       // Email.setText()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                // Name, email address, and profile photo Url
                val name = user.displayName
                val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid
            Email.setText("email : "+email)
        }




        signout_btn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            //Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }



        profileChangebtn.setOnClickListener {
            val intent = Intent(activity,ProfileChange::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid
            Nickname.setText("닉네임 : "+name)
        }
        super.onResume()
    }

    fun finish(){
        finish()
    }

}
