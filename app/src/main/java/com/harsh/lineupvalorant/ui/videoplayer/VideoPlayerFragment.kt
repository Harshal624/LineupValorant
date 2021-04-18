package com.harsh.lineupvalorant.ui.videoplayer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.harsh.lineupvalorant.R
import com.harsh.lineupvalorant.databinding.VideoPlayerFragmentBinding


class VideoPlayerFragment : Fragment(R.layout.video_player_fragment) {


    private var _binding: VideoPlayerFragmentBinding? = null
    private val binding: VideoPlayerFragmentBinding get() = _binding!!
    private val viewModel: VideoPlayerViewModel by viewModels()

    val args: VideoPlayerFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = VideoPlayerFragmentBinding.bind(view)
        binding.dmPlayerWebView.load(mapOf("video" to args.video.video_url))
    }

    override fun onPause() {
        super.onPause()
        binding.dmPlayerWebView.pause()
    }

    override fun onResume() {
        super.onResume()
        binding.dmPlayerWebView.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}