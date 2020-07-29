package kr.ac.kpu.kpu_t

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
open class ChatRoom(
    @PrimaryKey var id: Long = 0,
    var title: String = "",
    var start: String = "",
    var end: String = ""

) : RealmObject() {
}