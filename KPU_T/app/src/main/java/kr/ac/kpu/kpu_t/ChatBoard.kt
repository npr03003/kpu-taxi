package kr.ac.kpu.kpu_t

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ChatBoard(
    @PrimaryKey var cid: Long = 0,
    var ctitle: String = "",
    var cstart: String = "",
    var cend: String = "",
    var cmax : Int =0
) : RealmObject() {
}