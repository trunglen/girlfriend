package chat.girlfriend.findgirlfriend

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import chat.girlfriend.findgirlfriend.common.NewGirlService
import chat.girlfriend.findgirlfriend.models.Girl
import chat.girlfriend.findgirlfriend.ui.BaseActivity
import chat.girlfriend.findgirlfriend.ui.fragment.FavouriteFragment
import chat.girlfriend.findgirlfriend.ui.fragment.HotFragment
import chat.girlfriend.findgirlfriend.ui.fragment.NewFragment
import chat.girlfriend.findgirlfriend.utils.NotificationActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_ads.*


class MainActivity : BaseActivity() {
    companion object {
        var isRunning = true
    }

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
        startService(Intent(this, NewGirlService::class.java))
        requestAds(banner)
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                Log.d("lifecycle", "new fragment")
                return NewFragment()
            } else if (position == 1) {
                Log.d("lifecycle", "new HotFragment")
                return HotFragment()
            } else {
                Log.d("lifecycle", "new FavouriteFragment")
                return FavouriteFragment()
            }
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
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