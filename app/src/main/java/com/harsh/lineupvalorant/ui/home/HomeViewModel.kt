package com.harsh.lineupvalorant.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.harsh.lineupvalorant.api.DailyMotionApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*
 * We no long require to inject @Assisted
 * alpha 2.30+ has assisted included :D
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dailyMotionApi: DailyMotionApi,
    private val saveStateHandle: SavedStateHandle
) : ViewModel() {

}