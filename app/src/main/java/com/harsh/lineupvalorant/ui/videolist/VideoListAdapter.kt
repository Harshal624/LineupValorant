package com.harsh.lineupvalorant.ui.videolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.harsh.lineupvalorant.api.VimeoApi
import com.harsh.lineupvalorant.data.Video
import com.harsh.lineupvalorant.databinding.ItemVideoBinding

class VideoListAdapter(
    vimeoApi: VimeoApi,
    lifecycleCoroutineScope: LifecycleCoroutineScope
) : ListAdapter<Video, VideoListAdapter.VideoViewHolder>(VideoComparator()) {

    private var vimeoApi: VimeoApi? = null
    private var lifecycleCoroutineScope: LifecycleCoroutineScope? = null

    init {
        this.vimeoApi = vimeoApi
        this.lifecycleCoroutineScope = lifecycleCoroutineScope
    }


    inner class VideoViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(video: Video) {
            binding.apply {
                Glide.with(itemView).load(video.img_medium)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivThumbnail)
                tvTitle.text = video.title
                tvAgent.text = video.agent_name
                tvImgurl.text = video.img_small
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null)
            holder.bind(currentItem)
    }

}