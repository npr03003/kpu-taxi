package kr.ac.kpu.kpu_t

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.my_chat.view.*

class MyAdapter(private val myDataset: ArrayList<Chat>,private val name: String) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(val v: View) : RecyclerView.ViewHolder(v)
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.MyViewHolder {
        // create a new view
        if(viewType==1){
            var v : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_chat, parent, false) as View
            return MyViewHolder(v)
        }
        else{
            var v : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.other_chat, parent, false) as View
            return MyViewHolder(v)
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
            holder.v.tvChat.text = myDataset[position].message
            holder.v.tvChat_email.text= myDataset[position].Nick
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size

    override fun getItemViewType(position: Int): Int {
       //return super.getItemViewType(position)
        if(myDataset[position].Nick.equals(name)){
            return 1
        }
        else{
            return 2
        }
    }
}