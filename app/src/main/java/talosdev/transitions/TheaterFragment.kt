package talosdev.transitions

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager


private const val ARG_POSITION = "position"
private const val ARG_PARAM2 = "param2"


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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_theater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager: ViewPager = view.findViewById(R.id.viewPager)

        viewPager.adapter = TheaterViewPagerAdapter(fragmentManager!!, viewModel.liveData.value!!)

        viewPager.currentItem = position

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
        // TODO: Update argument type and name
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
