package kr.ac.kpu.kpu_t

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_profile_change.*
import kotlinx.android.synthetic.main.fragment_mypage.*

class ProfileChange : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_change)
        val user1 = FirebaseAuth.getInstance().currentUser
        user1?.let {
            // Name, email address, and profile photo Url
            val name = user1.displayName
            nick_editText.setHint(name)

        }

        Modify_btn.setOnClickListener{
            val user = FirebaseAuth.getInstance().currentUser
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(nick_editText.getText().toString())
                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build()

            user?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("TAG", "User profile updated.")
                    }
                }
            finish()
        }

    }
}
