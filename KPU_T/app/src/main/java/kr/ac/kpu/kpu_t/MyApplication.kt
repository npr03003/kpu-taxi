package kr.ac.kpu.kpu_t

import android.app.Application
import io.realm.Realm
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this) //Realm을 사용하기 위하여 초기화
    }
}