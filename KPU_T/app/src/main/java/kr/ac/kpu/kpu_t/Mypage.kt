package kr.ac.kpu.kpu_t



import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_mypage.*




/**
 * A simple [Fragment] subclass.
 */
class Mypage : Fragment() {




    private val multiplePermissionsCode = 100


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


        //로그아웃
        btn_logout.setOnClickListener {

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




        btn_logout.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }



        btn_profilechange.setOnClickListener {
            val intent = Intent(activity,ProfileChange::class.java)
            startActivity(intent)
        }
    }




        //프로필수정
        btn_profilechange.setOnClickListener {

            //공백
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        Toast.makeText(activity, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
    }

    fun finish(){
        finish()
    }


}
