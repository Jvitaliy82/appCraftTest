package com.jdeveloperapps.appcrafttest.di

import android.content.Context
import androidx.room.Room
import com.jdeveloperapps.appcrafttest.api.AlbumsApi
import com.jdeveloperapps.appcrafttest.db.AlbumDatabase
import com.jdeveloperapps.appcrafttest.util.Constants.ALBUM_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(AlbumsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideAlbumApi(retrofit: Retrofit): AlbumsApi =
        retrofit.create(AlbumsApi::class.java)

    @Provides
    @Singleton
    fun provideAlbumDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        AlbumDatabase::class.java,
        ALBUM_DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideAlbumDao(db: AlbumDatabase) = db.getAlbumDao()

}