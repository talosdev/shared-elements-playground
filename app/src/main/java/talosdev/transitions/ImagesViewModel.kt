package talosdev.transitions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created by Perry Street Software Inc. on Mar 04, 2019.
 *
 * @author Aris Papadopoulos (aris@scruff.com)
 */
class ImagesViewModel: ViewModel() {

    private val urls = (100..120).map{
        "https://picsum.photos/600/800?image=$it"
    }


    val liveData: MutableLiveData<List<String>> = MutableLiveData()

    init {
        liveData.value = urls
    }

}