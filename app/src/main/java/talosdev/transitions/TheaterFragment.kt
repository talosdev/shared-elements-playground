package talosdev.transitions

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_image.*
import kotlinx.android.synthetic.main.fragment_theater.*


private const val ARG_POSITION = "position"

class TheaterFragment : Fragment() {

    private var position: Int = 0
    private var listener: TheaterFragment.OnFragmentInteractionListener? = null
    private lateinit var viewModel: ImagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
        }

        viewModel = (activity as HasViewModel).getViewModel()

        setHasOptionsMenu(true)


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.theater, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.goToGrid -> {
                listener?.onTheaterInteraction()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_theater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager: ViewPager = view.findViewById(R.id.viewPager)

        viewPager.adapter = TheaterViewPagerAdapter(
            // TRANS important: must be child fragment manager so that child fragment can call getParent.
            childFragmentManager, viewModel.liveData.value!!)

        viewPager.currentItem = position

        viewPager.addOnPageChangeListener( object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                viewModel.currentPosition = position
            }

        })

        // must call prepare after the viewpager is set up
        prepareTransition()
        // TRANS
        // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation
        if (savedInstanceState == null) {
            postponeEnterTransition()
        }

    }

    private fun prepareTransition() {
        // TRANS 2 - Set enter transition
        val transition = TransitionInflater.from(context)
            .inflateTransition(R.transition.image_transition)
        sharedElementEnterTransition = transition


        // TRANS - mapping
        // A similar mapping is set at the GridFragment with a setExitSharedElementCallback.
        setEnterSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
                    // Locate the image view at the primary fragment (the ImageFragment that is currently
                    // visible). To locate the fragment, call instantiateItem with the selection position.
                    // At this stage, the method will simply return the fragment at the position and will
                    // not create a new one.
                    val currentFragment : ImageFragment =
                        viewPager.adapter?.instantiateItem(viewPager, viewModel.currentPosition) as ImageFragment

                    // Map the first shared element name to the child ImageView.
                    sharedElements[names[0]] = currentFragment.fullscreenImage
                }
            })

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TheaterFragment.OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onTheaterInteraction()
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int) =
            TheaterFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POSITION, position)
                }
            }
    }

}
