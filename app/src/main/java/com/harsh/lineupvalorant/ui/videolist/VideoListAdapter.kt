package com.harsh.lineupvalorant.ui.videolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.harsh.lineupvalorant.data.Video
import com.harsh.lineupvalorant.databinding.ItemVideoBinding

class VideoListAdapter(
    videoClickListener: OnVideoClickListener
) : ListAdapter<Video, VideoListAdapter.VideoViewHolder>(VideoComparator()) {

    var videoClickListener: OnVideoClickListener? = null
    init {
        this.videoClickListener = videoClickListener
    }

    inner class VideoViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    videoClickListener!!.onVideoClick(video = getItem(adapterPosition))
                }
            }
        }

        fun bind(video: Video) {
            binding.apply {
                if (video.img_medium.isNotEmpty()) {
                    Glide.with(itemView).load(video.img_large)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivThumbnail)
                }
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

    interface OnVideoClickListener {
        fun onVideoClick(video: Video)
    }

}