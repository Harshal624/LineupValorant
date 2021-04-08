package com.harsh.lineupvalorant.data

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
@Entity(tableName = "video_database")
data class Video(
    var title: String = "",
    var description: String = "",
    var date_added: Long = 0, //Epoch time
    @PrimaryKey(autoGenerate = false)
    var video_url: String = "",
    var agent_name: String = "",
    var map_name: String = "",
    var ability_type: String = "",
    var ispopular: Boolean = false,
    var site_name: String = "",
    @Exclude
    var video_duration: Long = 0,
    @Exclude
    var img_small: String = "",
    @Exclude
    var img_medium: String = "",
    @Exclude
    var img_large: String = "",
    @Exclude
    var isFavourite: Boolean = false,
    @Exclude
    var total_views: Int = 0
) : Parcelable