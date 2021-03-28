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

    @Query("DELETE from video_database")
    suspend fun deleteAllVideos()

    @Query("SELECT * FROM video_database")
    fun getAllVideos(): Flow<List<Video>>
}