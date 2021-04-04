package com.harsh.lineupvalorant.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.harsh.lineupvalorant.data.Video
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

    @Query("SELECT * FROM video_database WHERE title LIKE '%' || :searchQuery || '%' ORDER BY date_added DESC")
    fun getAllVideos(searchQuery: String): Flow<List<Video>>

    @Query("UPDATE video_database SET video_duration = :videoDuration, img_small = :imgSmall, img_medium = :imgMedium, img_large = :imgLarge WHERE video_url = :video_url")
    suspend fun updateVideoDetails(
        video_url: String,
        videoDuration: Long,
        imgSmall: String,
        imgMedium: String,
        imgLarge: String
    )
}