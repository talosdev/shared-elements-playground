package talosdev.transitions

import android.net.Uri
import android.os.Bundle
import android.view.View
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
                .add(R.id.frame, GridFragment.newInstance())
                .commit()
        }
    }

    override fun getViewModel(): ImagesViewModel {
        return viewModel
    }

    override fun onGridInteraction(position: Int) {
        supportFragmentManager.beginTransaction()
            .add(R.id.frame,TheaterFragment.newInstance(position))
            .addToBackStack(null)
            .commit()
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

interface HasViewModel {
    fun getViewModel(): ImagesViewModel
}
