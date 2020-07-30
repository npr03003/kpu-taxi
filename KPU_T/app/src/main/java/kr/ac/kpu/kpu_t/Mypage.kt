package kr.ac.kpu.kpu_t


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_mypage.*
<<<<<<< HEAD

=======
import androidx.fragment.app.Fragment
>>>>>>> e536ed7fb336ed20ee5bd1dbb6639aeead251c08

/**
 * A simple [Fragment] subclass.
 */
class Mypage : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        signout_btn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finish()
        }
    }

<<<<<<< HEAD
    override fun onDestroyView() {
        super.onDestroyView()
        Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
    }

=======
>>>>>>> e536ed7fb336ed20ee5bd1dbb6639aeead251c08
    fun finish(){
        finish()
    }

<<<<<<< HEAD



=======
    override fun onDestroyView() {
        super.onDestroyView()
        Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
    }
>>>>>>> e536ed7fb336ed20ee5bd1dbb6639aeead251c08

}
