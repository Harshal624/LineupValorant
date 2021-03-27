package com.harsh.lineupvalorant.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.harsh.lineupvalorant.R
import com.harsh.lineupvalorant.data.sync.LineupSharedPref
import com.harsh.lineupvalorant.databinding.HomeFragmentBinding
import com.harsh.lineupvalorant.utils.LUtils
import com.harsh.lineupvalorant.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment) {
    private var _binding: HomeFragmentBinding? = null
    private val binding: HomeFragmentBinding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = HomeFragmentBinding.bind(view)

        binding.apply {
            tvAllVideos.setOnClickListener {
                navigateToVideoListFragment(it, VID_TYPE_ALL)
            }
            tvAgent.setOnClickListener {
                navigateToVideoListFragment(it, VID_TYPE_AGENT)
            }
            tvMap.setOnClickListener {
                navigateToVideoListFragment(it, VID_TYPE_MAP)
            }
            tvPopular.setOnClickListener {
                navigateToVideoListFragment(it, VID_TYPE_POPULAR)
            }
            tvWallbangs.setOnClickListener {
                navigateToVideoListFragment(it, VID_TYPE_WALLBANGS)
            }
            LineupSharedPref.init(requireContext())
            workmanagerTv.text = LineupSharedPref.setValue
        }
        viewModel.res.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    it.data.let {
                        val list = it
                        for(video in list!!){
                            Timber.v("Video details: ${video}")
                        }
                    }
                }
                Status.LOADING -> {
                    Timber.v("Details loading...")
                }
                Status.ERROR -> {
                    Timber.v("Failed to retrieve videodetails error!")
                }
            }
        })


    }

    private fun navigateToVideoListFragment(view: View, videoType: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToVideoListFragment(
            videoType
        )
        view.findNavController().navigate(action)
        Timber.v("Current epoch time: ${LUtils.getCurrentTimeInEPOCH()}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val VID_TYPE_ALL = "All"
        const val VID_TYPE_WALLBANGS = "Wallbangs"
        const val VID_TYPE_POPULAR = "Popular"
        const val VID_TYPE_MAP = "Map"
        const val VID_TYPE_AGENT = "Agent"
    }
}