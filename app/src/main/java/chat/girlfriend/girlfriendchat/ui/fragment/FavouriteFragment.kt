package chat.girlfriend.girlfriendchat.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import chat.girlfriend.girlfriendchat.*
import chat.girlfriend.girlfriendchat.models.Girl
import chat.girlfriend.girlfriendchat.utils.getLocalStorage
import chat.girlfriend.girlfriendchat.utils.removeLocalStorage
import kotlinx.android.synthetic.main.fragment_favourite.*

class FavouriteFragment : Fragment() {
    var favouriteGirls = ArrayList<Girl>()
    lateinit var favouriteAdapter: GalleryGridViewAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_favourite, container, false)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        loadFavourite()
    }

    fun loadFavourite(): Unit {
        favouriteGirls = (activity!!.application as MyApplication).getLocalStorage()
        favouriteAdapter = GalleryGridViewAdapter(context!!, favouriteGirls, object : OnDeleteListener {
            override fun onDeleteListener(postition: Int) {
                favouriteGirls.removeAt(postition)
                (activity!!.application as MyApplication).removeLocalStorage(postition)
                Toast.makeText(activity, "Delete From Your Favourite", Toast.LENGTH_LONG).show()
            }
        })
        favouriteGrid.adapter = favouriteAdapter
        favouriteGrid.setOnItemClickListener { parent, view, position, id ->
            val girl = favouriteGirls.get(position)
            redirectToDetail(activity!!, girl)
        }
        favouriteAdapter.notifyDataSetChanged()
    }
}