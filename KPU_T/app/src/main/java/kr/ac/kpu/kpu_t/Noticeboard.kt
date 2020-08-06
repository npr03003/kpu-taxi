package kr.ac.kpu.kpu_t


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_noticeboard.*

/**
 * A simple [Fragment] subclass.
 */
class Noticeboard : Fragment() {

    val database = FirebaseDatabase.getInstance()
    val boardRef = database.getReference("board")
    var boardlist = arrayListOf<carfullBoard>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_noticeboard, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val intent = Intent(activity,carfull_Board_setting::class.java)
        createFab.setOnClickListener { startActivity(intent) }
        carfullView.setOnItemClickListener { adapterView, view, i, l ->

        }
    }
    private fun loadBoard(){
        val boardAdapter = context?.let { boardAdapter(it, boardlist) }
        boardRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                boardlist.clear()
                for(i in snapshot.children){
                    var id : String = ""
                    boardRef.child(i.key.toString()).addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var st :String=""
                            var ed :String=""
                            var tt:String=""
                            var mkr:String=""
                            id = i.value.toString()
                            for(j in snapshot.children){
                                if(j.key.equals("start")){
                                    st = j.value.toString()
                                } else if(j.key.equals("end")){
                                    ed = j.value.toString()
                                } else if(j.key.equals("title")){
                                    tt = j.value.toString()
                                } else if(j.key.equals("maker")){
                                    mkr = j.value.toString()
                                }
                            }
                            boardlist.add(carfullBoard(tt,st,ed,mkr,id))
                            carfullView.adapter = boardAdapter
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onStart() {
        super.onStart()
        loadBoard()

    }


}


class carfullBoard(var title:String, var start:String, var end:String, var maker:String, var boardid:String)


class boardAdapter(val context:Context, val boardlist: ArrayList<carfullBoard>):BaseAdapter(){
    override fun getView(position: Int, p1: View?, p2: ViewGroup?): View {
        val view : View = LayoutInflater.from(context).inflate(R.layout.item_board,null)

        val titleboard = view.findViewById<TextView>(R.id.boardTitle)
        val routeboard = view.findViewById<TextView>(R.id.boardroute)
        val boardnum = boardlist[position]
        val boardowner = boardnum.maker
        val boardid = boardnum.boardid
        titleboard.text = boardnum.title
        routeboard.text = "경로: "+boardnum.start+" -> "+boardnum.end


        return view
    }

    override fun getCount(): Int {
        return boardlist.size
    }

    override fun getItem(p0: Int): Any {
        return boardlist[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }
}
