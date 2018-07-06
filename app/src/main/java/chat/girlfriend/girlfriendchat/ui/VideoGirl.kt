package chat.girlfriend.girlfriendchat.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import chat.girlfriend.girlfriendchat.R
import kotlinx.android.synthetic.main.activity_video_girl.*

class VideoGirl : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_girl)
        vdGirl.setVideoPath("https://www.youtube.com/embed/W0Lf3mfRFoc")
        vdGirl.start()
    }
}
