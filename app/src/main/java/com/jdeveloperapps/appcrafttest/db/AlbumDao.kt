package com.jdeveloperapps.appcrafttest.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jdeveloperapps.appcrafttest.models.Album
import com.jdeveloperapps.appcrafttest.models.AlbumDetail

@Dao
interface AlbumDao {

    @Query("SELECT * from album_table")
    fun getAllAlbum() : LiveData<List<Album>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: Album, listAlbumDetail: List<AlbumDetail>)

    @Query("SELECT * FROM album_detail_table WHERE albumId = :id")
    fun getListAlbumDetailById(id: Int) : List<AlbumDetail>

    @Delete
    suspend fun deleteAlbum(album: Album, listAlbumDetail: List<AlbumDetail>)
}