package talosdev.transitions

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target


/**
 * Created by Perry Street Software Inc. on Mar 04, 2019.
 *
 * @author Aris Papadopoulos (aris@scruff.com)
 */
class GridAdapter(private val urls: List<String>,
                  private val viewModel: ImagesViewModel) : RecyclerView.Adapter<GridAdapter.ImageViewHolder>() {

    var listener: ImageListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_item, parent, false)
        return ImageViewHolder(view)

    }

    override fun getItemCount(): Int {
        return urls.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(urls[position])

        holder.imageView.setOnClickListener {
            listener?.onClick(holder.adapterPosition)
        }
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private var target: Target? = null


        fun bind(url: String) {
            // Clear previous image - dummy "placeholder"
            imageView.setImageBitmap(null)
            // TRANS
            imageView.transitionName = url

            target = object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    Log.d("PICASSO", "Prepare loading $url")

                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    Log.e("PICASSO", "Error $url", e)
                    viewModel.registerGridLoadComplete(adapterPosition)
                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    imageView.setImageBitmap(bitmap)
                    viewModel.registerGridLoadComplete(adapterPosition)
                }

            }
            Picasso.get().load(url).into(target as Target)


        }


    }

}

interface ImageListener {
    fun onClick(position: Int)
}