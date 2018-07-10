package chat.girlfriend.girlfriendchat

import android.app.Activity
import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import chat.girlfriend.girlfriendchat.models.Girl
import chat.girlfriend.girlfriendchat.utils.getLocalStorage
import chat.girlfriend.girlfriendchat.utils.removeLocalStorage
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_favourite.*
import kotlinx.android.synthetic.main.fragment_hot.*
import kotlinx.android.synthetic.main.fragment_main.*


class MainActivity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0)
//                return PlaceholderFragment.newInstance(position + 1)
                return NewFragment()
            else if (position == 1)
                return HotFragment()
            else
                return FavouriteFragment()
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class NewFragment : Fragment() {
        // Write a message to the database
        var database = FirebaseDatabase.getInstance()
        var myRef = database.getReference("girl")
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_main, container, false)
            return rootView
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            this.loadNew()
        }

        fun loadNew() {
            var girls = ArrayList<Girl>()
            var galleryAdapter = GalleryGridViewAdapter(context!!, girls)
            galleryHolder.adapter = galleryAdapter
            galleryHolder.setOnItemClickListener { parent, view, position, id ->
                val girl = girls.get(position)
                redirectToDetail(activity!!, girl)
            }

            myRef.orderByChild("time_stamp").addChildEventListener(object : ChildEventListener {
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
                    girl.id = p0!!.key
                    girls.add(girl)
                    galleryAdapter.notifyDataSetChanged()
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })
        }
    }

    class HotFragment : Fragment() {
        var database = FirebaseDatabase.getInstance()
        var myRef = database.getReference("girl")
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_hot, container, false)
            return rootView
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
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

            myRef.orderByChild("time_stamp").addChildEventListener(object : ChildEventListener {
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
                    Log.d("lifecycle", girl.name + girl.is_hot.toString())
                    if (girl.is_hot) {
                        girl.id = p0!!.key
                        girls.add(girl)
                        galleryAdapter.notifyDataSetChanged()
                    }
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })
        }
    }

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
}

fun redirectToDetail(original: Activity, girl: Girl) {
    val intent = Intent(original, DetailActivity::class.java)
    intent.putExtra("name", girl.name)
    intent.putExtra("id", girl.id)
    intent.putExtra("thumb", girl.thumb)
    intent.putExtra("fb_url", girl.fb_url)
    intent.putExtra("girl_gallery", girl.gallery)
    original.startActivity(intent)
}