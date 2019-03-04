package talosdev.transitions

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created by Perry Street Software Inc. on Mar 04, 2019.
 *
 * @author Aris Papadopoulos (aris@scruff.com)
 */
class TheaterViewPagerAdapter(
    fm: FragmentManager,
    private val urls: List<String>,
    private val listener: LoadImageListener
) : FragmentStatePagerAdapter(fm) {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = super.instantiateItem(container, position) as ImageFragment
        item.listener = listener
        return item
    }

    override fun getItem(position: Int): Fragment {
        return ImageFragment.newInstance(urls[position])

    }

    override fun getCount(): Int {
        return urls.size
    }
}