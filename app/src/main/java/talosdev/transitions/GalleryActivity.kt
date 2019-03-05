package talosdev.transitions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

class GalleryActivity : AppCompatActivity(),
    HasViewModel,
    GridFragment.OnFragmentInteractionListener,
    TheaterFragment.OnFragmentInteractionListener {

    private lateinit var viewModel: ImagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(ImagesViewModel::class.java)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val goToTheater = intent.getBooleanExtra(EXTRA_THEATER, false)

            if (goToTheater) {
                supportFragmentManager.beginTransaction()
                    .add(R.id.frame, TheaterFragment.newInstance(5), TAG_THEATER)
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .add(R.id.frame, GridFragment.newInstance(), TAG_GALLERY)
                    .commit()
            }
        }
    }

    override fun getViewModel(): ImagesViewModel {
        return viewModel
    }

    override fun onGridInteraction(position: Int, imageView: ImageView) {
        viewModel.currentPosition = position

        supportFragmentManager.beginTransaction()
            // TRANS - notify the fragment manager that we have a transition
            .setReorderingAllowed(true)
            .addSharedElement(imageView, imageView.transitionName)
            .replace(R.id.frame, TheaterFragment.newInstance(position), TAG_THEATER)
            .addToBackStack(null)
            .commit()
    }

    override fun onTheaterInteraction() {
//        val theaterFragment = supportFragmentManager.findFragmentByTag(TAG_THEATER)
//
//        supportFragmentManager.beginTransaction()
//            .remove(theaterFragment!!)
//            .commit()
//        supportFragmentManager.popBackStack()

        // For the time being, in order to be able to show animation when clicking the grid item,
        // do this TODO
        onBackPressed()
    }

    companion object {
        const val TAG_THEATER = "theater"
        private const val TAG_GALLERY = "gallery"
        private const val EXTRA_THEATER = "theater"

        fun navigate(context: Context, goToTheater: Boolean) {
            val intent = Intent(context, GalleryActivity::class.java)
                .putExtra(EXTRA_THEATER, goToTheater)
            context.startActivity(intent)
        }
    }
}


interface HasViewModel {
    fun getViewModel(): ImagesViewModel
}
