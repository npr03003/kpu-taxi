package kr.ac.kpu.kpu_t




import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_mypage.*


/**
 * A simple [Fragment] subclass.
 */
class Mypage : Fragment() {
    private val multiplePermissionsCode = 100
    private val TAG : String? = SplashActivity :: class.simpleName
    private  var mFirebaseStorage: FirebaseStorage=FirebaseStorage.getInstance()
    private var Userfilename:String=""
    private var isgetImage:Int=0 //0이면 이미지 없음, 1이면 이미지 있음
    private val database = FirebaseDatabase.getInstance()
    val Ref = database.getReference("user")
    val user = FirebaseAuth.getInstance().currentUser
    val uid = user!!.uid.toString()
    private var user2222:String=""
    private var stEmail:String=""
    private var isgranted2:Int=0

    private var filePath: Uri?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val sharedPref = activity?.getSharedPreferences("shared", Context.MODE_PRIVATE)
        stEmail= sharedPref!!.getString("email","").toString()
        Log.d(TAG,"stEmail : "+stEmail)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }

    override fun onStart() {
        super.onStart()


    }

    override fun onResume() {
        super.onResume()
        displayimage()
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var unknown: String = "알수없음"

        Ref.addListenerForSingleValueEvent(object : ValueEventListener {
            //데이터 불러오는
            override fun onDataChange(snapshot: DataSnapshot) {

                Ref.child(uid).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (x in snapshot.children) {
                            if (x.key.equals("name")) {
                                var nickname333: String = x.value.toString()
                                Nickname2.setText(nickname333)
                                user2222 = nickname333
                                displayimage()

                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        btn_sync.setOnClickListener {

            Ref.addListenerForSingleValueEvent(object : ValueEventListener {
                //데이터 불러오는
                override fun onDataChange(snapshot: DataSnapshot) {

                    Ref.child(uid).addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (x in snapshot.children) {
                                if (x.key.equals("name")) {
                                    var nickname333: String = x.value.toString()
                                    Nickname2.setText(nickname333)
                                }

                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    })
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
            displayimage()
            Toast.makeText(activity, "동기화 완료", Toast.LENGTH_SHORT).show()
        }

        //로그아웃
        //2020.08.05일에 남준이가 수정함 건들지 마셈
        btn_logout.setOnClickListener {
            signout()
        }


        //문의하기
        //2020.08.05일에 남준이가 수정함 건들지 마셈
        btn_question.setOnClickListener {
            questionStart()
        }


        //계정삭제
        //2020.08.05일에 남준이가 수정함 건들지 마셈
        btn_delete.setOnClickListener {
            val dlg3 = activity?.let { AlertDialog.Builder(it) }
            dlg3!!.setTitle("계정삭제").setMessage("정말로 계정을 삭제하시겠습니까?")
            dlg3.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                delete()
                Ref.child(uid).child("name").setValue(unknown)
                Ref.child(uid).child("gender").setValue(unknown)
            })
            dlg3.setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            dlg3.show()
        }


        //공지사항
        btn_notice.setOnClickListener {

            val intent = Intent(activity, GongG::class.java)
            startActivity(intent)
        }

        //이미지 넣기
        btn_imageSetting.setOnClickListener {
            val intent = Intent(activity, Setprofileimage::class.java)
            startActivity(intent)
        }


        //닉네임 설정 및 수정
        btn_setnickname.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
            val user = FirebaseAuth.getInstance().currentUser?.uid.toString()
            val userRef = database.getReference("user")

            userRef.child(user).child("nickset").addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val ischanged = snapshot.value.toString()
                    if(ischanged=="complete"){
                        val dlg3 = activity?.let { AlertDialog.Builder(it) }
                        dlg3!!.setTitle("닉네임 설정").setMessage("닉네임이 이미 설정되었습니다.")
                        dlg3.setPositiveButton(
                            "확인",
                            DialogInterface.OnClickListener { dialog, which ->
                                dialog.dismiss()
                            })
                        dlg3.show()
                    } else {
                        val intent = Intent(activity, SetNickname::class.java)
                        startActivity(intent)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })




        }




    }


    //2020.08.05일에 남준이가 수정함 건들지 마셈
    fun finish(){
        finish()
    }


    fun displayimage(){
        Userfilename=stEmail
        val storageRef:StorageReference=mFirebaseStorage.getReferenceFromUrl("gs://fir-113.appspot.com/")
        storageRef.child(Userfilename).child("images.png").getBytes(java.lang.Long.MAX_VALUE)
            .addOnSuccessListener(OnSuccessListener<ByteArray> { bytes ->
                Log.d(TAG, "getBytes Success")
                // Use the bytes to display the image
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                profile_image.visibility=View.GONE
                textview_noimage.visibility=View.GONE
                profile_image2.setImageBitmap(bmp)
                profile_image2.visibility=View.VISIBLE
            }).addOnFailureListener(OnFailureListener {
                Log.d(
                    TAG,
                    "getBytes Failed")
            })
    }

    fun delete(){
        user!!.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val dlg = activity?.let { AlertDialog.Builder(it) }
                    dlg!!.setTitle("계정삭제").setMessage("계정이 삭제되었습니다.")
                    dlg.show()
                    finish()
                }
                else{
                    Toast.makeText(activity,"삭제 실패!",Toast.LENGTH_LONG).show()
                }
            }
    }

    //2020.08.05일에 남준이가 수정함 건들지 마셈
    fun questionStart(){
        val uri = Uri.parse("tel:010-0000-0000")
        val intent = Intent(Intent.ACTION_DIAL, uri)
        startActivity(intent)
    }


    //2020.08.05일에 남준이가 수정함 건들지 마셈
    fun signout(){

        val dlg = activity?.let { AlertDialog.Builder(it) }
        dlg!!.setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
        dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(activity, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
            finish()

        })
        dlg.setNegativeButton("취소",
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
        dlg.show()

    }

}
