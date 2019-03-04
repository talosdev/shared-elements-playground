package talosdev.transitions

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class GridFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var viewModel: ImagesViewModel


    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

        viewModel = (activity as HasViewModel).getViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_grid, container, false)


        return view
    }

    private fun prepareTransitions() {

        exitTransition = TransitionInflater.from(context).inflateTransition(R.transition.grid_exit_transition)

        setExitSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(names: List<String>, sharedElements: MutableMap<String, View>) {
                    // Locate the ViewHolder for the clicked position.
                    val selectedViewHolder = recyclerView
                        .findViewHolderForAdapterPosition(viewModel.currentPosition)
                        ?: throw NullPointerException("no viewholder found")

                    // Map the first shared element name to the child ImageView.
                    sharedElements[names[0]] = (selectedViewHolder as GridAdapter.ImageViewHolder).imageView
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler)

        recyclerView.layoutManager = GridLayoutManager(context!!, 2, RecyclerView.VERTICAL, false)
        val adapter = GridAdapter(viewModel.liveData.value!!)
        recyclerView.adapter = adapter

        adapter.listener = object : ImageListener {
            override fun onClick(position: Int) {
                listener?.onGridInteraction(
                    position,
                    (recyclerView.findViewHolderForAdapterPosition(position) as GridAdapter.ImageViewHolder).imageView
                )
            }

            override fun onImageLoadComplete(success: Boolean, position: Int) {
                if (position == viewModel.currentPosition) {
                    startPostponedEnterTransition()
                }
            }
        }

        scrollToPosition()

        prepareTransitions()
        postponeEnterTransition()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * Scrolls the recycler view to show the last viewed item in the grid. This is important when
     * navigating back from the grid.
     */
    private fun scrollToPosition() {
        recyclerView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                recyclerView.removeOnLayoutChangeListener(this)
                val layoutManager = recyclerView.layoutManager
                val viewAtPosition = layoutManager!!.findViewByPosition(viewModel.currentPosition)
                // Scroll to position if the view for the current position is null (not currently part of
                // layout manager children), or it's not completely visible.
                if (viewAtPosition == null || layoutManager
                        .isViewPartiallyVisible(viewAtPosition, false, true)
                ) {
                    recyclerView.post { layoutManager.scrollToPosition(viewModel.currentPosition) }
                }
            }
        })
    }

    interface OnFragmentInteractionListener {
        fun onGridInteraction(position: Int, imageView: ImageView)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            GridFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
