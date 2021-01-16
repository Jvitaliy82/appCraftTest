package com.jdeveloperapps.appcrafttest.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "album_detail_table")
@Parcelize
data class AlbumDetail(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val albumId: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
) : Parcelable