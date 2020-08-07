package kr.ac.kpu.kpu_t

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_chatting_room.*

class ChattingRoom : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting_room)
        setTitle("채팅")
        var actionBar = actionBar
        actionBar?.run {
            setDisplayHomeAsUpEnabled(true)
        }
        btn_send.setOnTouchListener { _: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    btn_send.setBackgroundColor(Color.BLUE)
                }
                MotionEvent.ACTION_UP -> {
                    btn_send.setBackgroundColor(Color.parseColor("#ffffA500"))
                }
            }
            true
        }
    }
}