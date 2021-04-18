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
    var map: Int = 0,
    var ability: String = "",
    var ability_type: Int = 0,
    /*
     * A site = 1
     * B site = 2
     * C site = 3
     */
    var site: Int = 0,
    var video_download_url: String = "",
    @Exclude
    var video_duration: Long = 0,
    @Exclude
    var img_small: String = "",
    @Exclude
    var img_medium: String = "",
    @Exclude
    var img_large: String = "",
    @Exclude
    var total_views: Int = 0

) : Parcelable {

    companion object {

        //Sites
        const val SITE_A = 1
        const val SITE_B = 2
        const val SITE_C = 3

        //Maps
        const val MAP_SPLIT = 1
        const val MAP_ASCENT = 2
        const val MAP_BIND = 3
        const val MAP_HAVEN = 4
        const val MAP_ICEBOX = 5

        //Ability type
        const val ABILITY_TYPE_SIMPLE = 1
        const val ABILITY_TYPE_RETAKE = 2
        const val ABILITY_TYPE_AFTERPLANT = 3
        const val ABILITY_TYPE_ONEWAY = 4

        //TODO ADD MORE CONSTANTS FOR ABILITIES, AGENTS
        /*
         * The more constans(integers), the faster is the firestore fetch and better the user experience :)
         * 'Strings sucks'
         * Only the title, video_url, video_download_url, description need to be strings
         */
    }
}