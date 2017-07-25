package technology.mainthread.apps.gatekeeper.view.adapter

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.squareup.picasso.Picasso

import technology.mainthread.apps.gatekeeper.R
import technology.mainthread.apps.gatekeeper.databinding.ItemImageBinding

class ImagesAdapter(private val picasso: Picasso, private val images: List<Uri>) : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemImageBinding = ItemImageBinding.bind(view)

        fun bindTo(picasso: Picasso, imageUri: Uri) {
            picasso.load(imageUri).into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(picasso, images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

}
