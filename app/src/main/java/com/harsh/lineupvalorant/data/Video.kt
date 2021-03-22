package com.harsh.lineupvalorant.data

data class Video(
    val title: String?,
    val description: String?,
    val date_added: Long?, //Epoch time
    val video_url: String?,
    val agent_name: String?,
    val map_name: String?,
    val ability_type: String?,
    val ispopular: Boolean?,
    val site_name: String?
) {
}