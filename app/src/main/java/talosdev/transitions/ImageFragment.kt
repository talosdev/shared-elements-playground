package talosdev.transitions


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso


private const val ARG_IMAGE_URL = "image_url"


class ImageFragment : Fragment() {
    private var imageUrl: String? = null
    lateinit var listener: LoadImageListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageUrl = it.getString(ARG_IMAGE_URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageView = view.findViewById<ImageView>(R.id.fullscreenImage)
        imageView.transitionName = imageUrl


        val target = object : com.squareup.picasso.Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                Log.d("PICASSO", "Prepare")
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                Log.e("PICASSO", "Error", e)
                listener.onImageLoad(false)
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                imageView.setImageBitmap(bitmap)

                // TRANS
                listener.onImageLoad(true)
            }
        }

        Picasso.get().load(imageUrl).into(target)


    }

    companion object {
        @JvmStatic
        fun newInstance(imageUrl: String) =
            ImageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_URL, imageUrl)
                }
            }
    }

}


interface LoadImageListener {
    fun onImageLoad(success: Boolean)
}
