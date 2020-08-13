package kr.ac.kpu.kpu_t

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater.*
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_noticeboard.*

class GongG : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticeboard)

        val listView = findViewById<ListView>(R.id.listview)

//        어답터 설정
        listView.adapter = MyCustomAdapter(this)

//        Item click listener
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectItem = parent.getItemAtPosition(position) as String
            selectName.text = selectItem
            //Toast.makeText(this, selectItem, Toast.LENGTH_SHORT).show()
        }
    }
}
class MyCustomAdapter(context: Context) : BaseAdapter() {
    private val mContext: Context

    //데이터 어레이
    private val names = arrayListOf<String>(
        "공지사항1", "공지사항2", "공지사항3", "공지사항4", "공지사항5"
    )

    init {
        mContext = context
    }
    override fun getCount(): Int {
        return names.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getItem(position: Int): Any {
        val selectItem = names.get(position)
        return selectItem
    }
    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val layoutInflater = from(mContext)
        val rowMain = layoutInflater.inflate(R.layout.row_main, viewGroup, false)

        val nameTextView = rowMain.findViewById<TextView>(R.id.name_textview)
        nameTextView.text = names.get(position)
        val positionTextView = rowMain.findViewById<TextView>(R.id.position_textview)
        positionTextView.text = "공지1" + position

        return rowMain
    }
}
