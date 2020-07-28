package kr.ac.kpu.kpu_t

import android.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class PagerAdapter(fm:FragmentManager): FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return when (position) { //switch()문과 동일하다.
            0 -> {Chatting()}
            1 -> {Mypage()}
            else -> {return Noticeboard()}
        }


    }

    override fun getCount(): Int {
        return 3 //3개니깐
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> "택시 채팅방"
            1 -> "마이페이지"
            else -> {return "카풀게시판"}
        }
    }
}