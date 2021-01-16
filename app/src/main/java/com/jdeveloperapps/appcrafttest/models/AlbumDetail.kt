package com.jdeveloperapps.appcrafttest.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album_detail_table")
data class AlbumDetail(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val albumId: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
)
