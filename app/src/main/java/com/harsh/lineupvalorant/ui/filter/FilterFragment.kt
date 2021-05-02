package com.harsh.lineupvalorant.ui.filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.harsh.lineupvalorant.R
import com.harsh.lineupvalorant.databinding.FilterFragmentBinding

class FilterFragment : Fragment(R.layout.filter_fragment) {

    private var _binding: FilterFragmentBinding? = null
    private val binding: FilterFragmentBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}