package talosdev.transitions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        Picasso.get().load(ImagesViewModel.urls[5]).into(chatImageView)

        button.setOnClickListener {
            GalleryActivity.navigate(this, false)
        }

        chatImageView.setOnClickListener {
            GalleryActivity.navigate(this, true)
        }
    }
}
