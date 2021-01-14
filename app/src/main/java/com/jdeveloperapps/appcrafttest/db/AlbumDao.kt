package com.jdeveloperapps.appcrafttest.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.jdeveloperapps.appcrafttest.models.Album

@Dao
interface AlbumDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: Album)

    @Delete
    suspend fun deleteAlbum(album: Album)
}