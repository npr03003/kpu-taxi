package kr.ac.kpu.kpu_t

import android.view.LayoutInflater
import android.widget.TextView
import android.view.View
import android.view.ViewGroup
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter

class carfullBoardAdapter(realmResult: OrderedRealmCollection<carfullBoard>) :
    RealmBaseAdapter<carfullBoard>(realmResult) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val bh: BoardView
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_board, parent, false)
            bh = BoardView(view)
            view.tag = bh
        }
        else{
            view = convertView
            bh = view.tag as BoardView
        }

        if(adapterData != null){
            val item = adapterData!![position]
            bh.titleTV.text = item.cftitle
            bh.pathTV.text = "경로 : "+item.cfstart+" -> "+item.cfend
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
    val titleTV: TextView = view.findViewById(R.id.boardTitle)
    val pathTV: TextView = view.findViewById(R.id.boardroute)
}