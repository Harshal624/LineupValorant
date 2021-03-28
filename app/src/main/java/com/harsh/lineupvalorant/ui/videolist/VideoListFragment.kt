package com.harsh.lineupvalorant.ui.videolist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.harsh.lineupvalorant.R
import com.harsh.lineupvalorant.databinding.VideoListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

/*
 *Displays a list of videos
 *Accepts a video_type argument which should be fetched and when a user clicks on the filter FAB, that filter(e.g.Agent) should be auto selected
 */

@AndroidEntryPoint
class VideoListFragment : Fragment(R.layout.video_list_fragment) {
    private var _binding: VideoListFragmentBinding? = null
    private val binding: VideoListFragmentBinding get() = _binding!!
    private val viewModel: VideoListViewModel by viewModels()

    private val args: VideoListFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = VideoListFragmentBinding.bind(view)
        binding.tvVideoType.text = args.videoType

        val adapter = VideoListAdapter()

        binding.apply {
            recyclerview.adapter = adapter
            recyclerview.layoutManager = LinearLayoutManager(requireContext())
            recyclerview.setHasFixedSize(true)
        }
        viewModel.videoDetails.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}