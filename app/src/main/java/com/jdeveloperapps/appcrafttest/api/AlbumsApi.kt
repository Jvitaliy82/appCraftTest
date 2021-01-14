package com.jdeveloperapps.appcrafttest.api

import com.jdeveloperapps.appcrafttest.models.Album
import com.jdeveloperapps.appcrafttest.models.AlbumDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AlbumsApi {

    companion object {
        val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }

    @GET("albums")
    suspend fun getAlbums() : Response<List<Album>>

    @GET("photos")
    suspend fun getPhotos(
        @Query("albumId") id: Int
    ) : Response<List<AlbumDetail>>
}