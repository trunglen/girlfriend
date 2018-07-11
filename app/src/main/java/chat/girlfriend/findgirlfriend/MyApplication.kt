package chat.girlfriend.findgirlfriend

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class MyApplication : Application() {
    lateinit var sharedPreferences :SharedPreferences
    override fun onCreate() {
        super.onCreate()
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this)
    }
}