package talosdev.transitions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat.*


class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val url = ImagesViewModel.urls[5]

        Picasso.get().load(url).into(chatImageView)
        chatImageView.transitionName = url

        button.setOnClickListener {
            startActivity(GalleryActivity.getIntent(this, false))
        }

        chatImageView.setOnClickListener {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@ChatActivity,
                chatImageView,
                ViewCompat.getTransitionName(chatImageView) ?: ""
            )
            startActivity(GalleryActivity.getIntent(this, true), options.toBundle())
        }

    }
}
