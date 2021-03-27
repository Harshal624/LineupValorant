package com.harsh.lineupvalorant.data

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude

@Keep
@Entity(tableName = "video_database")
data class Video(
    val title: String?,
    val description: String?,
    val date_added: Long?, //Epoch time
    @PrimaryKey(autoGenerate = false)
    val video_url: String?,
    val agent_name: String?,
    val map_name: String?,
    val ability_type: String?,
    val ispopular: Boolean?,
    val site_name: String?,
    @Exclude
    val isFavourite: Boolean?
) {
}