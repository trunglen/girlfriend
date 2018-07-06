package chat.girlfriend.girlfriendchat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import chat.girlfriend.girlfriendchat.ui.GirlInfoActivity
import chat.girlfriend.girlfriendchat.ui.VideoGirl
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fab_layout.*

class DetailActivity : AppCompatActivity() {
    var showFab = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        var fbUrl = intent.extras.get("fb_url").toString()
        var girlGallery = intent.extras.get("girl_gallery")
        val galleryUrl = ArrayList<String>()
        val adapter = DetailListViewAdapter(this, girlGallery as ArrayList<String>)
        lvGirls.adapter = adapter
        btnMainMenu.setOnClickListener {
            if (showFab) hideMenu() else showMenu()
        }
        btnViewVideo.setOnClickListener {
            val intent = Intent(this, VideoGirl::class.java)
            startActivity(intent)
        }
        btnAddFriend.setOnClickListener {
            val intent = Intent(this, GirlInfoActivity::class.java)
            intent.putExtra("fb_url", fbUrl)
            startActivity(intent)
        }
//        myRef.child(girlId.toString()).addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onDataChange(p0: DataSnapshot?) {
//                val girl =  p0!!.getValue<Girl>(Girl::class.java)!!
//                galleryUrl.addAll(girl.gallery)
//                adapter.notifyDataSetChanged()
//            }
//
//        })

    }

    fun showMenu() {
        btnAddFriend.show()
        btnViewVideo.show()
        showFab = true
    }

    fun hideMenu() {
        btnAddFriend.hide()
        btnViewVideo.hide()
        showFab = false
    }

}
