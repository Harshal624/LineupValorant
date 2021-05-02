package com.harsh.lineupvalorant.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.harsh.lineupvalorant.data.Video
import com.harsh.lineupvalorant.ui.videolist.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<Video>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleVideo(video: Video)

    @Query("DELETE from video_database")
    suspend fun deleteAllVideos()


    @Query("SELECT COUNT(*) FROM video_database")
    suspend fun getVideoCount(): Int

    @Query("SELECT * FROM video_database WHERE (isFavourite != :hideFavourites OR isFavourite = 0) AND title LIKE '%' || :searchQuery || '%' ORDER BY date_added DESC, date_added")
    fun getAllVideosSortedByDate(searchQuery: String, hideFavourites: Boolean): Flow<List<Video>>

    @Query("SELECT * FROM video_database WHERE (isFavourite != :hideFavourites OR isFavourite = 0) AND title LIKE '%' || :searchQuery || '%' ORDER BY date_added DESC, total_views")
    fun getAllVideosSortedByViews(searchQuery: String, hideFavourites: Boolean): Flow<List<Video>>

    @Query("UPDATE video_database SET video_duration = :videoDuration, img_small = :imgSmall, img_medium = :imgMedium, img_large = :imgLarge, total_views = :totalViews WHERE video_url = :video_url")
    suspend fun updateVideoDetails(
        video_url: String,
        videoDuration: Long,
        imgSmall: String,
        imgMedium: String,
        imgLarge: String,
        totalViews: Int
    )


    fun getVideos(query: String, sortOrder: SortOrder, hideFavourites: Boolean): Flow<List<Video>> =
        when (sortOrder) {
            SortOrder.BY_DATE -> {
                getAllVideosSortedByDate(query, hideFavourites)
            }
            SortOrder.BY_VIEWS -> {
                getAllVideosSortedByViews(query, hideFavourites)
            }
        }

}