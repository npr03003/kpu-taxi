package kr.ac.kpu.kpu_t

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class carfullBoard(
    @PrimaryKey var cfid: Long = 0,
    var cftitle: String = "",
    var cfstart: String = "",
    var cfend: String = ""
    ) : RealmObject() {
}