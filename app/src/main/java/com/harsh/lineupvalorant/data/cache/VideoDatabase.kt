package com.harsh.lineupvalorant.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.harsh.lineupvalorant.data.Video


/*
 * //TODO add some initial videos to the first load of the database
 * 20 videos would work
 */
@Database(entities = [Video::class], version = 1)
abstract class VideoDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
}