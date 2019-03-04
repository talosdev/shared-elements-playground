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
import com.squareup.picasso.Target


private const val ARG_IMAGE_URL = "image_url"


class ImageFragment : Fragment() {
    private var imageUrl: String? = null
    private var target: Target? = null

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


        target = object : com.squareup.picasso.Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                Log.d("PICASSO", "Prepare")
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                Log.e("PICASSO", "Error", e)
//                listener?.onImageLoad(false)
                val parent = parentFragment as Fragment
                parent.startPostponedEnterTransition()
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                Log.e("PICASSO", "Loaded")
                imageView.setImageBitmap(bitmap)

                // TRANS
                val parent = parentFragment as Fragment
                parent.startPostponedEnterTransition()            }
        }

        Picasso.get().load(imageUrl).into(target!!)


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


