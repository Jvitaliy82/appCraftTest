package com.jdeveloperapps.appcrafttest.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jdeveloperapps.appcrafttest.models.Album

@Database(
    entities = [Album::class],
    version = 1
)
abstract class AlbumDatabase : RoomDatabase() {

    abstract fun getAlbumDao(): AlbumDao

}