package kr.ac.kpu.kpu_t


import androidx.fragment.app.Fragment


import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PagerAdapter(fm:FragmentManager): FragmentPagerAdapter(fm) {


    //2020.08.05 남준이가 수정함
    private val items: ArrayList<Fragment>
    private val itext:ArrayList<String>

    //2020.08.05 남준이가 수정함
    init {
        itext=ArrayList<String>()
        items = ArrayList<Fragment>()
        items.add(Chatting())
        items.add(Noticeboard())
        items.add(Mypage())

        itext.add("택시채팅방")
        itext.add("카풀게시판")
        itext.add("마이페이지")

    }
    //2020.08.05 남준이가 수정함
    override fun getItem(position: Int): Fragment {
        return items[position]
    }
    //2020.08.05 남준이가 수정함
    override fun getCount(): Int {
        return items.size
    }
    //2020.08.05 남준이가 수정함
    override fun getPageTitle(position: Int): CharSequence? {
        return itext.get(position)
    }


}