package chat.girlfriend.girlfriendchat.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import chat.girlfriend.girlfriendchat.R
import kotlinx.android.synthetic.main.activity_girl_info.*

class GirlInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_girl_info)
//        var fbUrl = intent.extras.get("fb_url")
//        Log.d("fb_url", fbUrl as String?)
        wvFb.loadUrl("http://dantri.com.vn/")
        wvFb.settings.loadsImagesAutomatically = true
    }
}
