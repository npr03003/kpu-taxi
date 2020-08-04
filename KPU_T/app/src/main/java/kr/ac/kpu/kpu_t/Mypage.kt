package kr.ac.kpu.kpu_t


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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //로그아웃
        btn_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finish()
        }

        //프로필수정
        btn_profilechange.setOnClickListener {

            //
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
