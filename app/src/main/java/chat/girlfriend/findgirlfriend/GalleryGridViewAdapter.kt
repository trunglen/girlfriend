package chat.girlfriend.findgirlfriend

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.view.LayoutInflater
import chat.girlfriend.findgirlfriend.models.Girl
import kotlinx.android.synthetic.main.item_gallery.view.*
import truyencuoioffline.truyencuoi.truyencuoi.extensions.inflateLayout
import truyencuoioffline.truyencuoi.truyencuoi.extensions.loadImage


class GalleryGridViewAdapter(val context: Context, val listGirls: List<Girl>, val isFavourite: OnDeleteListener? = null) : BaseAdapter() {

//    constructor(context: Context, listGirls: List<Girl>) : this() {
//        this.context = context
//        this.girls = listGirls
//    }

    lateinit var deleteListener: OnDeleteListener
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var girl = this.listGirls.get(position)
        var view = this.context.inflateLayout(R.layout.item_gallery)
        view.imvGirlThumb.loadImage(girl.thumb)
        view.tvName.text = girl.name
        if (isFavourite!=null) {
            view.btnDelete.visibility = View.VISIBLE
            view.btnDelete.setOnClickListener {
                this.isFavourite.onDeleteListener(position)
                this.notifyDataSetChanged()
            }
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return this.listGirls.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return this.listGirls.size
    }


}

interface OnDeleteListener {
    public fun onDeleteListener(postition: Int)

}