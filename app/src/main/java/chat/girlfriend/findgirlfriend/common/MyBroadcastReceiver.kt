package chat.girlfriend.findgirlfriend.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import chat.girlfriend.findgirlfriend.models.Girl
import chat.girlfriend.findgirlfriend.utils.NotificationActivity

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val extra = intent!!.extras
        val girl = Girl(
                extra.getString("id"),
                extra.getString("name"),
                extra.getString("thumb"),
                extra.getStringArrayList("girl_gallery"),
                extra.getString("fb_url")
        )
        if (context != null) {
            NotificationActivity.showNotify(context, girl)
        }
    }
}