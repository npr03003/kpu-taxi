package kr.ac.kpu.kpu_t



import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_mypage.*
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class Mypage : Fragment() {
    private val multiplePermissionsCode = 100
    private val TAG : String? = MainActivity :: class.simpleName
    private lateinit var mFirebaseStorage: FirebaseStorage


    val user = FirebaseAuth.getInstance().currentUser
    private var filePath: Uri?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val uid = user!!.uid.toString()
        val database = FirebaseDatabase.getInstance()
        val Ref = database.getReference("user")
        mFirebaseStorage= FirebaseStorage.getInstance()
        var unknown:String="알수없음"



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


        //로그아웃
        //2020.08.05일에 남준이가 수정함 건들지 마셈
        btn_logout.setOnClickListener {
            signout()
        }

        //프로필 수정
        //아직 수정 안했음
        btn_profilechange.setOnClickListener {
            val intent = Intent(activity, ProfileChange::class.java)
            startActivity(intent)
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

        //프로필수정
        btn_profilechange.setOnClickListener {
            //공백
        }

        //공지사항
        btn_notice.setOnClickListener {

        }

        //닉네임 설정
        btn_setnickname.setOnClickListener {
            val intent = Intent(activity, SetNickname::class.java)
            startActivity(intent)
        }

    }

    //2020.08.05일에 남준이가 수정함 건들지 마셈
    fun finish(){
        finish()
    }

    //2020.08.07일에 남준이가 수정함 건들지 마셈
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                try {
                    filePath=data!!.data
                    // 선택한 이미지에서 비트맵 생성
                    val in2: InputStream = context!!.contentResolver.openInputStream(data!!.data!!)!!
                    val img = BitmapFactory.decodeStream(in2)
                    in2!!.close()
                    // 이미지 표시
                    textview_noimage.visibility=View.GONE
                    profile_image.visibility=View.GONE
                    profile_image2.setImageBitmap(img)
                    profile_image2.visibility=View.VISIBLE
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }


    //파일 업로드
    private fun uploadFile(filepath22:Uri) {
        //업로드할 파일이 있으면 수행
        if (filepath22 != null) {
            //업로드 진행 Dialog 보이기
            val progressDialog = ProgressDialog(activity)
            progressDialog.setTitle("업로드중...")
            progressDialog.show()

            //storage
            val storage = FirebaseStorage.getInstance()

            //Unique한 파일명을 만들자.
            val formatter = SimpleDateFormat("yyyyMMHH_mmss")
            val now = Date()
            val filename = formatter.format(now) + ".png"
            //storage 주소와 폴더 파일명을 지정해 준다.
            val storageRef = storage.getReferenceFromUrl("gs://fir-113.appspot.com/")
                .child("images/$filename")
            //업로드 ㄱㄱ
            storageRef.putFile(filepath22)
                //성공시
                .addOnSuccessListener {
                    progressDialog.dismiss() //업로드 진행 Dialog 상자 닫기
                    Toast.makeText(activity, "업로드 완료!", Toast.LENGTH_SHORT)
                        .show()
                }
                //실패시
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(activity, "업로드 실패!", Toast.LENGTH_SHORT)
                        .show()
                }
                //진행중
                .addOnProgressListener { taskSnapshot ->
                    val progress =
                        (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toDouble()//이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                    //dialog에 진행률을 퍼센트로 출력해 준다
                    progressDialog.setMessage("Uploaded " + progress.toInt() + "% ...")
                }
        } else {
            Toast.makeText(activity, "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT)
                .show()
        }
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
