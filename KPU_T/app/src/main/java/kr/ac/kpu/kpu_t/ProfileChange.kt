package kr.ac.kpu.kpu_t

import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.change_profile.*

class ProfileChange: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.change_profile)

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

        Modify_btn.setOnClickListener{
            finish()
        }
    }
}