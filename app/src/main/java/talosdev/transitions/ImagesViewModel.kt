package talosdev.transitions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlin.random.Random

/**
 * Created by Perry Street Software Inc. on Mar 04, 2019.
 *
 * @author Aris Papadopoulos (aris@scruff.com)
 */
class ImagesViewModel : ViewModel() {

    companion object {
        val random = Random(12)

        val urls = (110..150).map {
            val height = random.nextInt(600, 1000)
            val width = random.nextInt(500, 800)
            "https://picsum.photos/$width/$height?image=$it"
        }
    }

    var currentPosition: Int = -1

    val liveData: MutableLiveData<List<String>> = MutableLiveData()
    private val _loadCompleteStream : PublishSubject<LoadComplete> = PublishSubject.create()
    val loadCompleteStream: Observable<LoadComplete> = _loadCompleteStream

    init {
        liveData.value = urls
    }

    fun registerGridLoadComplete(position: Int) {
        if (position == currentPosition) {
            _loadCompleteStream.onNext(LoadComplete.GridLoadComplete)
        }
    }

    fun registerTheaterLoadComplete() {
        _loadCompleteStream.onNext(LoadComplete.TheaterLoadComplete)
    }

}

sealed class LoadComplete {
    object GridLoadComplete : LoadComplete()
    object TheaterLoadComplete : LoadComplete()
}