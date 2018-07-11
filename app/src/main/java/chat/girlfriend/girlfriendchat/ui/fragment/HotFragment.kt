package chat.girlfriend.girlfriendchat.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import chat.girlfriend.girlfriendchat.GalleryGridViewAdapter
import chat.girlfriend.girlfriendchat.MainActivity
import chat.girlfriend.girlfriendchat.R
import chat.girlfriend.girlfriendchat.models.Girl
import chat.girlfriend.girlfriendchat.redirectToDetail
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_hot.*
import kotlinx.android.synthetic.main.layout_spinner.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import java.util.concurrent.atomic.AtomicInteger


class HotFragment : Fragment() {
    var database = FirebaseDatabase.getInstance()
    var myRef = database.getReference("girl")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_hot, container, false)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        activity!!.spin_kit.visibility = View.VISIBLE
        loadHot()
    }

    fun loadHot() {
        var girls = ArrayList<Girl>()
        var galleryAdapter = GalleryGridViewAdapter(context!!, girls)
        hotGrid.adapter = galleryAdapter
        hotGrid.setOnItemClickListener { parent, view, position, id ->
            val girl = girls.get(position)
            redirectToDetail(activity!!, girl)
        }
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
                if (girl.hot) {
                    girl.id = p0!!.key
                    girls.add(girl)
                    galleryAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        val count = AtomicInteger()

        activity!!.runOnUiThread {
            Handler().postDelayed({
                if (MainActivity.isRunning) {
                    activity!!.spin_kit.visibility = View.INVISIBLE
                }
            }, 8000)
        }
    }
}