package chat.girlfriend.findgirlfriend.utils

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.Context
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.support.v4.app.NotificationCompat
import chat.girlfriend.findgirlfriend.common.MyBroadcastReceiver
import android.content.Context.NOTIFICATION_SERVICE
import android.app.NotificationManager
import chat.girlfriend.findgirlfriend.DetailActivity
import chat.girlfriend.findgirlfriend.R
import chat.girlfriend.findgirlfriend.models.Girl


class NotificationActivity {
    companion object {
        val requestCode = 1
        fun showNotify(context: Context, girl: Girl) {
            val mBuilder = NotificationCompat.Builder(context.applicationContext)
                    .setSmallIcon(R.drawable.ic_launcher_48)
                    .setContentTitle("New girl!!! ")
                    .setContentText("Do you want to add  your girl friend!!!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            val mNotifyMgr = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            // Builds the notification and issues it.
            mNotifyMgr.notify(requestCode, mBuilder.build())
            val resultIntent = Intent(context.getApplicationContext(), DetailActivity::class.java)
            resultIntent.putExtra("name", girl.name)
            resultIntent.putExtra("id", girl.id)
            resultIntent.putExtra("thumb", girl.thumb)
            resultIntent.putExtra("fb_url", girl.fb_url)
            resultIntent.putExtra("girl_gallery", girl.gallery)
            val resultPendingIntent = PendingIntent.getActivity(
                    context.getApplicationContext(),
                    requestCode,
                    resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            // Set content intent;
            mBuilder.setContentIntent(resultPendingIntent)
        }
    }

}