package com.jdeveloperapps.appcrafttest.api

import com.jdeveloperapps.appcrafttest.models.Album
import retrofit2.Response
import retrofit2.http.GET

interface AlbumsApi {

    companion object {
        val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }

    @GET("albums")
    suspend fun getAlbums() : Response<List<Album>>
}