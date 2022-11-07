package com.harifrizki.storyapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.harifrizki.storyapp.utils.EMPTY_STRING

@Entity(tableName = "storyEntity")
data class StoryEntity(

    @PrimaryKey
    @ColumnInfo(defaultValue = EMPTY_STRING)
    @field:SerializedName("id")
    var id: String = EMPTY_STRING,

    @field:SerializedName("name")
    var name: String? = EMPTY_STRING,

    @field:SerializedName("description")
    var description: String? = EMPTY_STRING,

    @field:SerializedName("photoUrl")
    var photoUrl: String? = EMPTY_STRING,

    @field:SerializedName("createdAt")
    var createdAt: String? = EMPTY_STRING,

    @field:SerializedName("lat")
    var lat: Float? = 0.0F,

    @field:SerializedName("long")
    var long: Float? = 0.0F
)

@Entity(tableName = "remoteKeysEntity")
data class RemoteKeysEntity(
    @PrimaryKey
    var id: String = EMPTY_STRING,
    var prevKey: Int? = 0,
    var nextKey: Int? = 0
)