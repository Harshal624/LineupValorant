package com.harsh.lineupvalorant.ui.videolist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.harsh.lineupvalorant.R
import com.harsh.lineupvalorant.databinding.HomeFragmentBinding
import com.harsh.lineupvalorant.databinding.VideoListFragmentBinding
import com.harsh.lineupvalorant.ui.home.HomeFragmentDirections

/*
 *Displays a list of videos
 *Accepts a video_type argument which should be fetched and when a user clicks on the filter FAB, that filter(e.g.Agent) should be auto selected
 */

class VideoListFragment : Fragment(R.layout.video_list_fragment) {
    private var _binding: VideoListFragmentBinding? = null
    private val binding: VideoListFragmentBinding get() = _binding!!

    private val args: VideoListFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = VideoListFragmentBinding.bind(view)
        binding.tvVideoType.text = args.videoType
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}