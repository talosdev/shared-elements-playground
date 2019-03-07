package talosdev.transitions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

class GalleryActivity : AppCompatActivity(),
    HasViewModel,
    GridFragment.OnFragmentInteractionListener,
    TheaterFragment.OnFragmentInteractionListener {

    private lateinit var viewModel: ImagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        postponeEnterTransition()

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

    override fun onTheaterInteraction(imageView: View) {

        val gridFragment = supportFragmentManager.findFragmentByTag(TAG_GALLERY)
        val theaterFragment = supportFragmentManager.findFragmentByTag(TAG_THEATER)

        if (gridFragment == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .addSharedElement(imageView, imageView.transitionName)
                .replace(R.id.frame, GridFragment.newInstance(), TAG_GALLERY)
                .commit()
        } else {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        val gridFragment = supportFragmentManager.findFragmentByTag(TAG_GALLERY)
        val theaterFragment = supportFragmentManager.findFragmentByTag(TAG_THEATER)

        if (supportFragmentManager.backStackEntryCount == 0
            && gridFragment != null) {
            overridePendingTransition(0, 0)
        }
        super.onBackPressed()
    }

    companion object {
        const val TAG_THEATER = "theater"
        private const val TAG_GALLERY = "gallery"
        private const val EXTRA_THEATER = "theater"

        fun getIntent(context: Context, goToTheater: Boolean): Intent {
            return Intent(context, GalleryActivity::class.java)
                .putExtra(EXTRA_THEATER, goToTheater)
        }
    }
}


interface HasViewModel {
    fun getViewModel(): ImagesViewModel
}
