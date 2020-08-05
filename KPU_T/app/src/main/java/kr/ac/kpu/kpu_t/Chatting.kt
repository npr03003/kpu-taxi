package kr.ac.kpu.kpu_t


import android.content.Intent
import kotlinx.android.synthetic.main.fragment_chatting.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmBaseAdapter
import io.realm.kotlin.where


/**
 * A simple [Fragment] subclass.
 */
class Chatting : Fragment() {
    val realm = Realm.getDefaultInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chatting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val intent = Intent(activity, TaxiRoomSetting::class.java)
        val realmResult = realm.where<ChatBoard>().findAll()
        val adapter = ChatBoardAdapter(realmResult)
        listView.adapter = adapter
        realmResult.addChangeListener { _ -> adapter.notifyDataSetChanged() }
        plusFab.setOnClickListener { startActivity(intent) }
    }
}

class ChatBoardAdapter(realmResult: OrderedRealmCollection<ChatBoard>) :
    RealmBaseAdapter<ChatBoard>(realmResult) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val h: ChatView
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_room, parent, false)
            h = ChatView(view)
            view.tag = h
        }
        else{
            view = convertView
            h = view.tag as ChatView
        }

        if(adapterData != null){
            val item = adapterData!![position]
            h.chatTitle.text = item.ctitle
            h.chatPath.text = "경로 : "+item.cstart+" -> "+item.cend
            if(item.cmax == 4){
                h.threeSelectView.visibility = View.VISIBLE
                h.fourSelectView.visibility = View.VISIBLE
            }
            else if(item.cmax == 3){
                h.threeSelectView.visibility = View.VISIBLE
            }
        }
        return view
    }
    override fun getItemId(position: Int): Long {
        if (adapterData != null){
            return adapterData!![position].cid
        }
        return super.getItemId(position)
    }
}
class ChatView(view: View){
    val chatTitle: TextView = view.findViewById(R.id.text1)
    val chatPath: TextView = view.findViewById(R.id.text2)
    val threeSelectView : ImageView = view.findViewById(R.id.threeImg)
    val fourSelectView : ImageView = view.findViewById(R.id.fourImg)
}