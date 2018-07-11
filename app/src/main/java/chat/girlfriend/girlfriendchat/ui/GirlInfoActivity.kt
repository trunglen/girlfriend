package chat.girlfriend.girlfriendchat.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import chat.girlfriend.girlfriendchat.R
import kotlinx.android.synthetic.main.activity_girl_info.*
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import kotlinx.android.synthetic.main.item_ads.*


class GirlInfoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_girl_info)
        var fbUrl = intent.extras.get("fb_url")
        Log.d("fb_url", fbUrl.toString())

        wvFb.setWebChromeClient(WebChromeClient())
        wvFb.setWebViewClient(WebViewClient())
        wvFb.getSettings().setJavaScriptEnabled(true)
        wvFb.loadUrl(fbUrl.toString())
        requestAds(banner)
    }
}
