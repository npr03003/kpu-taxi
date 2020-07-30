package kr.ac.kpu.kpu_t


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.fragment_chatting.*
import kotlinx.android.synthetic.main.fragment_noticeboard.*

/**
 * A simple [Fragment] subclass.
 */
class Noticeboard : Fragment() {

    val realm = Realm.getDefaultInstance()

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
        val realmResult = realm.where<carfullBoard>().findAll()
        val adapter = carfullBoardAdapter(realmResult)
        carfullView.adapter = adapter
        realmResult.addChangeListener { _ -> adapter.notifyDataSetChanged() }
        createFab.setOnClickListener { startActivity(intent) }
    }


}
