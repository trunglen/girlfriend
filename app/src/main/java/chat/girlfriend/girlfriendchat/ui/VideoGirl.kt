package chat.girlfriend.girlfriendchat.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import chat.girlfriend.girlfriendchat.R
import kotlinx.android.synthetic.main.activity_video_girl.*
import android.view.View.SYSTEM_UI_FLAG_VISIBLE
import android.view.WindowManager
import android.view.View.SYSTEM_UI_FLAG_LOW_PROFILE
import android.webkit.WebView



class VideoGirl : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_girl)
//        vdGirl.setVideoPath("https://www.w3schools.com/html/mov_bbb.mp4")
//        vdGirl.start()
        val html = "<!DOCTYPE html> \n" +
                "<html> \n" +
                "<body style=\"background:black\"> \n" +
                "\n" +
                "<video width=\"100%\" controls>\n" +
                "  <source src=\"https://www.w3schools.com/html/mov_bbb.mp4\" type=\"video/mp4\">\n" +
                "</video>\n" +
                "</body> \n" +
                "</html>\n"
        val loadingView = layoutInflater.inflate(R.layout.view_loading_video, null)
       val  webChromeClient = object : VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) // See all available constructors...
        {
            // Subscribe to standard events, such as onProgressChanged()...
            override fun onProgressChanged(view: WebView, progress: Int) {
                // Your code...
            }
        }
        webChromeClient.setOnToggledFullscreen(object : VideoEnabledWebChromeClient.ToggledFullscreenCallback {
            override fun toggledFullscreen(fullscreen: Boolean) {
                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
                if (fullscreen) {
                    val attrs = window.attributes
                    attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
                    attrs.flags = attrs.flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    window.attributes = attrs
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
                    }
                } else {
                    val attrs = window.attributes
                    attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
                    attrs.flags = attrs.flags and WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON.inv()
                    window.attributes = attrs
                    if (android.os.Build.VERSION.SDK_INT >= 14) {
                        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                    }
                }

            }
        })
        webView.setWebChromeClient(webChromeClient)

        // Navigate everywhere you want, this classes have only been tested on YouTube's mobile site
        webView.loadUrl("https://www.w3schools.com/html/mov_bbb.mp4")
    }
}
