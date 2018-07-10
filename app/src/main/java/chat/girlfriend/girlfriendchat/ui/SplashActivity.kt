package chat.girlfriend.girlfriendchat.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import chat.girlfriend.girlfriendchat.R
import android.content.Intent
import chat.girlfriend.girlfriendchat.MainActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    private val SPLASH_DELAY = 1500

    private val mHandler = Handler()
    private val mLauncher = Launcher()

    override fun onStart() {
        super.onStart()

        mHandler.postDelayed(mLauncher, SPLASH_DELAY.toLong())
    }


    override fun onStop() {
        mHandler.removeCallbacks(mLauncher)
        super.onStop()
    }

    private fun launch() {
        if (!isFinishing) {
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }
    }

    private inner class Launcher : Runnable {
        override fun run() {
            launch()
        }
    }
}
