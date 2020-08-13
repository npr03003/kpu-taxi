package kr.ac.kpu.kpu_t

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_set_profile_image.*

import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class Setprofileimage : AppCompatActivity() {

    private  var mFirebaseStorage: FirebaseStorage=FirebaseStorage.getInstance()
    private val TAG = "SetImage"
    val user = FirebaseAuth.getInstance().currentUser
    private  lateinit var storage: FirebaseStorage
    val uid = user!!.uid.toString()
    val database = FirebaseDatabase.getInstance()
    val Ref = database.getReference("user")
    private var stEmail:String=""




    private var filePath: Uri? = null
    private var filename:String=""
    private var filename2:String=""
    private var userfilename:String=""

    override fun onResume() {
        super.onResume()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_profile_image)

        val sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE)
        stEmail= sharedPref.getString("email","").toString()
        Log.d(TAG,"stEmail : "+stEmail)




        //갤러리 열기
        setImage_btn_gallery.setOnClickListener{

            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0)

        }

        //setImage_displayimage()
        //파이어베이스 스토리지 업로드
        setImage_btn_upload.setOnClickListener{

            //이미지 넣었을 때
            Ref.child(uid).child("images").setValue("1")
            uploadFile()
        }


        //데이터받아옴
        //2020.08.06일에 남준이가 수정함 건들지 마셈
        Ref.addListenerForSingleValueEvent(object : ValueEventListener {//데이터 불러오는
        override fun onDataChange(snapshot: DataSnapshot)  {

            Ref.child(uid).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(x in snapshot.children) {
                        if(x.key.equals("name")){
                            var nickname333:String=x.value.toString()
                            setImage_textview.setText(nickname333)
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
    }


        //결과 처리
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
            if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
                filePath = data!!.data
                Log.d(TAG, "uri:" + java.lang.String.valueOf(filePath))
                try {
                    //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                    setImage_profile_image.visibility= View.GONE
                    setImage_textview_noimage.visibility=View.GONE
                    setImage_profile_image2.setImageBitmap(bitmap)
                    setImage_profile_image2.visibility=View.VISIBLE
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    fun setImage_displayimage(){
        userfilename=stEmail
        val storageRef: StorageReference =mFirebaseStorage.getReferenceFromUrl("gs://fir-113.appspot.com/")
        storageRef.child(userfilename).child("images.png").getBytes(java.lang.Long.MAX_VALUE)
            .addOnSuccessListener(OnSuccessListener<ByteArray> { bytes ->
                Log.d(TAG, "getBytes Success")
                // Use the bytes to display the image
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                setImage_profile_image.visibility=View.GONE
                setImage_textview_noimage.visibility=View.GONE
                setImage_profile_image2.setImageBitmap(bmp)
                setImage_profile_image2.visibility=View.VISIBLE
            }).addOnFailureListener(OnFailureListener {
                Log.d(
                    TAG,
                    "getBytes Failed")
             })
    }


    //upload the file
    private fun uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("업로드중...")
            progressDialog.show()

            //storage
            //Unique한 파일명을 만들자.
            val formatter = SimpleDateFormat("yyyyMMHH_mmss")
            val now = Date()
            userfilename=stEmail+"/"
            filename= userfilename+formatter.format(now).toString() + ".png"
            filename2=userfilename+"images.png"


            //storage 주소와 폴더 파일명을 지정해 준다.

            val storage = Firebase.storage("gs://fir-113.appspot.com")
            val storageRef =storage.reference

            val mountainsRef = storageRef.child(filename2)

            //올라가거라...
            mountainsRef.putFile(filePath!!) //성공시
                .addOnSuccessListener {
                    progressDialog.dismiss() //업로드 진행 Dialog 상자 닫기
                    alert("이미지가 업로드되었습니다."){
                        yesButton { finish() }
                    }.show()
                } //실패시
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "업로드 실패!", Toast.LENGTH_SHORT)
                        .show()
                } //진행중
                .addOnProgressListener { taskSnapshot ->
                    val progress =
                        100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                            .toDouble()
                    //dialog에 진행률을 퍼센트로 출력해 준다
                    progressDialog.setMessage("Uploaded " + progress.toInt() + "% ...")
                }
        } else {
            Toast.makeText(applicationContext, "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show()
        }
    }








}
