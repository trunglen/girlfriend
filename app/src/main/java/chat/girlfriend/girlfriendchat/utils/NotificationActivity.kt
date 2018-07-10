package chat.girlfriend.girlfriendchat.utils

import android.R
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.Context
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.support.v4.app.NotificationCompat
import chat.girlfriend.girlfriendchat.common.MyBroadcastReceiver
import android.content.Context.NOTIFICATION_SERVICE
import android.app.NotificationManager
import chat.girlfriend.girlfriendchat.DetailActivity
import chat.girlfriend.girlfriendchat.models.Girl


class NotificationActivity {
    companion object {
        fun showNotify(context: Context, girl: Girl) {
            val mBuilder = NotificationCompat.Builder(context.applicationContext)
                    .setSmallIcon(R.drawable.sym_def_app_icon)
                    .setContentTitle("New girl!!! ")
                    .setContentText("Do you want to add  your girl friend!!!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            val mNotifyMgr = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            // Builds the notification and issues it.
            mNotifyMgr.notify(1, mBuilder.build())


            val resultIntent = Intent(context.getApplicationContext(), DetailActivity::class.java)
            resultIntent.putExtra("name", girl.name)
            resultIntent.putExtra("id", girl.id)
            resultIntent.putExtra("thumb", girl.thumb)
            resultIntent.putExtra("fb_url", girl.fb_url)
            resultIntent.putExtra("girl_gallery", girl.gallery)

            val resultPendingIntent = PendingIntent.getActivity(
                    context.getApplicationContext(),
                    0,
                    resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )
            // Set content intent;
            mBuilder.setContentIntent(resultPendingIntent)
        }
    }

}