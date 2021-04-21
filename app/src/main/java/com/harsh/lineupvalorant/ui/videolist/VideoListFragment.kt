package com.harsh.lineupvalorant.ui.videolist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.harsh.lineupvalorant.R
import com.harsh.lineupvalorant.api.DailyMotionApi
import com.harsh.lineupvalorant.data.Video
import com.harsh.lineupvalorant.databinding.VideoListFragmentBinding
import com.harsh.lineupvalorant.utils.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

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

    @Inject
    lateinit var dailyMotionApi: DailyMotionApi

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = VideoListFragmentBinding.bind(view)
        binding.tvVideoType.text = args.videoType
        navController = findNavController()

        val adapter = VideoListAdapter(this)

        binding.apply {
            recyclerview.adapter = adapter
            //recyclerview.layoutManager = LinearLayoutManager(requireContext())
            recyclerview.setHasFixedSize(true)
        }
        viewModel.videoDetails.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        binding.searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onVideoClick(video: Video) {
        val action = VideoListFragmentDirections.actionVideoListFragmentToVideoPlayerFragment(video)
        navController.navigate(action)
    }
}