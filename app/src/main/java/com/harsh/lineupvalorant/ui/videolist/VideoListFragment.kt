package com.harsh.lineupvalorant.ui.videolist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.harsh.lineupvalorant.R
import com.harsh.lineupvalorant.data.Video
import com.harsh.lineupvalorant.databinding.VideoListFragmentBinding
import com.harsh.lineupvalorant.utils.LUtils
import com.harsh.lineupvalorant.utils.exhaustive
import com.harsh.lineupvalorant.utils.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect

/*
 *Displays a list of videos
 *Accepts a video_type argument which should be fetched and when a user clicks on the filter FAB, that filter(e.g.Agent) should be auto selected
 */

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class VideoListFragment : Fragment(R.layout.video_list_fragment),
    VideoListAdapter.OnVideoClickListener {
    private var _binding: VideoListFragmentBinding? = null
    private val binding: VideoListFragmentBinding get() = _binding!!
    private val viewModel: VideoListViewModel by viewModels()

    private val args: VideoListFragmentArgs by navArgs()
    private lateinit var navController: NavController

    private var job: Job? = null

    private lateinit var searchView: SearchView

    private lateinit var adapter: VideoListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = VideoListFragmentBinding.bind(view)
        setHasOptionsMenu(true)
        navController = findNavController()

        val shimmer = LUtils.getShimmerEffect()

        adapter = VideoListAdapter(this, shimmer)

        binding.apply {
            recyclerview.adapter = adapter
            recyclerview.setHasFixedSize(true)

            fabFilter.setOnClickListener {
                viewModel.videoType?.let {
                    viewModel.onFilterClick(it)
                }
            }
        }
        viewModel.videoDetails.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        job = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.videoEvent.collect {
                when (it) {
                    is VideoEvent.NavigateToFilterVideoScreen -> {
                        //Navigate to the filter screen
                        val direction =
                            VideoListFragmentDirections.actionVideoListFragmentToFilterFragment()
                        navController.navigate(direction)
                    }
                }.exhaustive
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_video_list, menu)

        val searchItem = menu.findItem(R.id.searchView)
        searchView = searchItem.actionView as SearchView

        /**
         * For some reason, when the screen is rotated while the searchview is active(expanded) with some
         * query in it, the searchview is collapsed.
         * To fix this we have to read the previous query which could be obtained from the viewmodel, then
         * expand the search widget and fill the searchview when onCreateOptionsMenu is called
         */
        val pendingQuery = viewModel.searchQuery.value
        if (!pendingQuery.isNullOrEmpty()) {
            //Expand the search view widget
            searchItem.expandActionView()
            //Fill the query
            searchView.setQuery(pendingQuery, false)
        }
        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_by_date -> {
                viewModel.sortOrder.value = SortOrder.BY_DATE
                true
            }
            R.id.sort_by_views -> {
                viewModel.sortOrder.value = SortOrder.BY_VIEWS
                true
            }
            R.id.action_hide_favourites -> {
                item.isChecked = !item.isChecked
                viewModel.hideFavourites.value = item.isChecked
                true
            }
            R.id.refresh -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }.exhaustive
    }

    override fun onStop() {
        super.onStop()
        if (job!!.isActive) {
            job!!.cancel()
        }
    }

    override fun onVideoClick(video: Video) {
        val action = VideoListFragmentDirections.actionVideoListFragmentToVideoPlayerFragment(video)
        navController.navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
        _binding = null
    }
}