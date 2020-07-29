package kr.ac.kpu.kpu_t


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_chatting.*

/**
 * A simple [Fragment] subclass.
 */
class Chatting : Fragment() {
    val realm = Realm.getDefaultInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val intent = Intent(getActivity(), TaxiRoomSetting::class.java)
        val realmResult = realm.where<ChatRoom>().findAll()
        val adapter = TaxiRoomAdapter(realmResult)
        listView.adapter = adapter
        realmResult.addChangeListener { _ -> adapter.notifyDataSetChanged()}
        plusFab.setOnClickListener { startActivity(intent) }
    }
}