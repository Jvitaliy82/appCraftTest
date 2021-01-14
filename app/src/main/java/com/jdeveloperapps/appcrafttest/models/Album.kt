package com.jdeveloperapps.appcrafttest.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "album_table")
@Parcelize
data class Album(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String
) : Parcelable

