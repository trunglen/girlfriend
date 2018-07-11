package chat.girlfriend.girlfriendchat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import chat.girlfriend.girlfriendchat.models.Girl
import chat.girlfriend.girlfriendchat.ui.BaseActivity
import chat.girlfriend.girlfriendchat.ui.DetailImageAdapter
import chat.girlfriend.girlfriendchat.ui.GirlInfoActivity
import chat.girlfriend.girlfriendchat.ui.VideoGirl
import chat.girlfriend.girlfriendchat.utils.GIRL_STORAGE
import chat.girlfriend.girlfriendchat.utils.existGirl
import chat.girlfriend.girlfriendchat.utils.getLocalStorage
import chat.girlfriend.girlfriendchat.utils.putLocalStorage
import com.himangi.imagepreview.ImagePreviewActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fab_layout.*
import kotlinx.android.synthetic.main.item_ads.*

class DetailActivity : BaseActivity() {
    var showFab = false
    lateinit var girl: Girl
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        var name = intent.extras.get("name").toString()
        var id = intent.extras.get("id").toString()
        var thumb = intent.extras.get("thumb").toString()
        var fbUrl = intent.extras.get("fb_url").toString()
        var girlGallery = intent.extras.get("girl_gallery")
        girl = Girl(id, name, thumb, girlGallery as ArrayList<String>, fbUrl)
//        val adapter = DetailListViewAdapter(this, girlGallery as ArrayList<String>)
        val detailAdapter = DetailImageAdapter(this, girlGallery as ArrayList<String>)
        lvGirls.adapter = detailAdapter
        lvGirls.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, ImagePreviewActivity::class.java);
            intent.putExtra(ImagePreviewActivity.IMAGE_LIST, girlGallery);
            intent.putExtra(ImagePreviewActivity.CURRENT_ITEM, position);
            startActivity(intent);
        }

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
        btnAddFavourite.setOnClickListener {

            if ((application as MyApplication).getLocalStorage().existGirl(girl.id)) {
                Toast.makeText(this, "Girl Exists Favourite List", Toast.LENGTH_LONG).show()
            } else {
                (application as MyApplication).putLocalStorage(girl)
                Toast.makeText(this, "Added To Favourite List", Toast.LENGTH_LONG).show()
            }
        }
        requestAds(banner)
    }

    fun showMenu() {
        btnAddFriend.show()
        btnAddFavourite.show()
        btnViewVideo.show()
        showFab = true
    }

    fun hideMenu() {
        btnAddFriend.hide()
        btnAddFavourite.hide()
        btnViewVideo.hide()
        showFab = false
    }

}
