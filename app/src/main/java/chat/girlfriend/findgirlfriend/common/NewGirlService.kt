package chat.girlfriend.findgirlfriend.common

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import chat.girlfriend.findgirlfriend.models.Girl
import chat.girlfriend.findgirlfriend.utils.NotificationActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class NewGirlService : Service() {
    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("girl")
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("service_new","start")
        myRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                var girl = p0!!.getValue<Girl>(Girl::class.java)!!
                NotificationActivity.showNotify(baseContext, girl)
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        return super.onStartCommand(intent, flags, startId)

    }
}
