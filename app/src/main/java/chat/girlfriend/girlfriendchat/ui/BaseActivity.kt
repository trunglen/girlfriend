package chat.girlfriend.girlfriendchat.ui

import android.support.v7.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

open class BaseActivity :AppCompatActivity() {
    fun requestAds(ads: AdView) {
        MobileAds.initialize(this,"ca-app-pub-9374507533753194~2708681754")
        val adRequest = AdRequest.Builder().build()
        ads.loadAd(adRequest)
    }
}