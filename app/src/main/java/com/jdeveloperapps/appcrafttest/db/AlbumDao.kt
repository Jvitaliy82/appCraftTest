package com.jdeveloperapps.appcrafttest.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jdeveloperapps.appcrafttest.models.Album

@Dao
interface AlbumDao {

    @Query("SELECT * from album_table")
    fun getAllAlbum() : LiveData<List<Album>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: Album)

    @Delete
    suspend fun deleteAlbum(album: Album)
}