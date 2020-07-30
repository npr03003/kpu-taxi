package kr.ac.kpu.kpu_t

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter

class carfullBoardAdapter(realmResult: OrderedRealmCollection<carfullBoard>) :
    RealmBaseAdapter<carfullBoard>(realmResult) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val vh: BoardView
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_room, parent, false)
            vh = BoardView(view)
            view.tag = vh
        }
        else{
            view = convertView
            vh = view.tag as BoardView
        }

        if(adapterData != null){
            val item = adapterData!![position]
            vh.titleTextView.text = item.cftitle
            vh.pathTextView.text = "경로 : "+item.cfstart+" -> "+item.cfend
        }

        return view
    }
    override fun getItemId(position: Int): Long {
        if (adapterData != null){
            return adapterData!![position].cfid
        }
        return super.getItemId(position)
    }
}

class BoardView(view: View){
    val titleTextView: TextView = view.findViewById(R.id.text1)
    val pathTextView: TextView = view.findViewById(R.id.text2)
}