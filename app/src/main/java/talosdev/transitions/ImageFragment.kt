package talosdev.transitions


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target


private const val ARG_IMAGE_URL = "image_url"


class ImageFragment : Fragment() {
    private var imageUrl: String? = null
    private var target: Target? = null

    private lateinit var viewModel: ImagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(activity!!).get(ImagesViewModel::class.java)

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
        imageView.transitionName = "theater_$imageUrl"


        target = object : com.squareup.picasso.Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                Log.d("PICASSO", "Prepare")
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                Log.e("PICASSO", "Error", e)
                viewModel.registerTheaterLoadComplete()
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                Log.e("PICASSO", "Loaded")
                imageView.setImageBitmap(bitmap)

                // TRANS
                viewModel.registerTheaterLoadComplete()
            }
        }

        Picasso.get().load(imageUrl).into(target!!)

        imageView.setOnTouchListener(SwipeToDismissListener(imageView))


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

    inner class SwipeToDismissListener(private val imageView: ImageView) : View.OnTouchListener {
        private var y0 = 0f
        private var currentScale = 1f
        private val scaleThreshold = 0.75f

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            return when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    y0 = event.rawY
                    imageView.pivotY = 0f
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val dy = event.rawY - y0
                    currentScale = 1 - dy / resources.displayMetrics.heightPixels

                    imageView.translationY = dy
                    imageView.scaleX = currentScale
                    imageView.scaleY = currentScale
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (currentScale >= scaleThreshold) {
                        imageView.animate().scaleX(1f).scaleY(1f).translationY(0f).start()
                    } else {
                        activity?.onBackPressed()
                    }
                    true
                }
                else -> {
                    false
                }
            }

        }

    }

}
