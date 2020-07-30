package kr.ac.kpu.kpu_t

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter
import org.jetbrains.anko.find

class TaxiRoomAdapter(realmResult: OrderedRealmCollection<ChatRoom>) :
    RealmBaseAdapter<ChatRoom>(realmResult) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val vh: ViewHolder
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_room, parent, false)
            vh = ViewHolder(view)
            view.tag = vh
        }
        else{
            view = convertView
            vh = view.tag as ViewHolder
        }

        if(adapterData != null){
            val item = adapterData!![position]
            vh.titleTextView.text = item.title
            vh.pathTextView.text = "경로 : "+item.start+" -> "+item.end
            if(item.number == 4){
                vh.threeSelectView.visibility = View.VISIBLE
                vh.fourSelectView.visibility = View.VISIBLE
            }
            else if(item.number == 3){
                vh.threeSelectView.visibility = View.VISIBLE
            }
        }

        return view
    }

    override fun getItemId(position: Int): Long {
        if (adapterData != null){
            return adapterData!![position].id
        }
        return super.getItemId(position)
    }
}
class ViewHolder(view: View){
    val titleTextView: TextView = view.findViewById(R.id.text1)
    val pathTextView: TextView = view.findViewById(R.id.text2)
    val threeSelectView : ImageView = view.findViewById(R.id.threeImg)
    val fourSelectView : ImageView = view.findViewById(R.id.fourImg)
}