package chat.girlfriend.findgirlfriend

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_detail.view.*
import truyencuoioffline.truyencuoi.truyencuoi.extensions.inflateLayout
import truyencuoioffline.truyencuoi.truyencuoi.extensions.loadImage

class DetailListViewAdapter(val context: Context, val gallery: ArrayList<String>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = context.inflateLayout(R.layout.item_detail)
        view.imvGirlGallery.loadImage(this.gallery.get(position))
        return view
    }

    override fun getItem(position: Int): Any {
        return this.gallery.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return this.gallery.size
    }

}