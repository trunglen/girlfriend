package chat.girlfriend.girlfriendchat

import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import chat.girlfriend.girlfriendchat.models.Girl
import chat.girlfriend.girlfriendchat.utils.getLocalStorage
import chat.girlfriend.girlfriendchat.utils.removeLocalStorage
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.activity_main.*
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
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : Fragment() {
        // Write a message to the database
        var database = FirebaseDatabase.getInstance()
        var myRef = database.getReference("girl")
        var girls = ArrayList<Girl>()
        var firstFavourite = false
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_main, container, false)
            firstFavourite = true
            return rootView
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            if (arguments?.get(ARG_SECTION_NUMBER) == 1) {
                this.loadNew()
            } else {
                loadFavourite()
                Log.d("change_tab", "fav")
            }

        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }

        fun loadFavourite(): Unit {
            var girlList = ArrayList<Girl>()
            lateinit var adapter: GalleryGridViewAdapter
            girlList = (activity!!.application as MyApplication).getLocalStorage()
            if (firstFavourite) {
                adapter = GalleryGridViewAdapter(context!!, girlList, object : OnDeleteListener {
                    override fun onDeleteListener(postition: Int) {
                        girlList.removeAt(postition)
                        (activity!!.application as MyApplication).removeLocalStorage(postition)
                        Toast.makeText(activity, "Delete From Your Favourite", Toast.LENGTH_LONG).show()
                    }

                })
                galleryHolder.adapter = adapter
                firstFavourite = false
            }
            adapter.notifyDataSetChanged()
            Log.d("local_storage", girlList.size.toString())
        }

        fun loadNew() {
            var galleryAdapter = GalleryGridViewAdapter(context!!, girls)
            galleryHolder.adapter = galleryAdapter
            galleryHolder.setOnItemClickListener { parent, view, position, id ->
                val intent = Intent(activity, DetailActivity::class.java)
                val girl = girls.get(position)
                intent.putExtra("name", girl.name)
                intent.putExtra("id", girl.id)
                intent.putExtra("thumb", girl.thumb)
                intent.putExtra("fb_url", girl.fb_url)
                intent.putExtra("girl_gallery", girl.gallery)
                startActivity(intent)
            }
            myRef.orderByKey().addChildEventListener(object : ChildEventListener {
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
}
