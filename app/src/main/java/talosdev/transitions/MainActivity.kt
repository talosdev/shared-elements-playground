package talosdev.transitions

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

class MainActivity : AppCompatActivity(),
    HasViewModel,
    GridFragment.OnFragmentInteractionListener,
    TheaterFragment.OnFragmentInteractionListener {

    private lateinit var viewModel: ImagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ImagesViewModel::class.java)

        setContentView(R.layout.activity_main)

        val frame = findViewById<View>(R.id.frame)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.frame, GridFragment.newInstance(), TAG_GALLERY)
                .commit()
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
        private const val TAG_THEATER = "theater"
        private const val TAG_GALLERY = "gallery"
    }
}


interface HasViewModel {
    fun getViewModel(): ImagesViewModel
}
