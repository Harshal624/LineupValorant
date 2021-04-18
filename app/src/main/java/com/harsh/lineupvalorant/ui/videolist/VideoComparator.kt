package com.harsh.lineupvalorant.ui.videolist

import androidx.recyclerview.widget.DiffUtil
import com.harsh.lineupvalorant.data.Video

class VideoComparator : DiffUtil.ItemCallback<Video>() {
    override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean =
        oldItem.video_url == newItem.video_url

    override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean =
        oldItem == newItem
}