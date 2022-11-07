package com.harifrizki.storyapp.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.harifrizki.storyapp.model.RemoteKeysEntity
import com.harifrizki.storyapp.model.Story
import com.harifrizki.storyapp.model.StoryEntity

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStory(story: ArrayList<StoryEntity>?)

    @Query("SELECT * FROM storyEntity")
    fun getAllStory(): PagingSource<Int, StoryEntity>

    @Query("DELETE FROM storyEntity")
    fun deleteAll()
}

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(remoteKey: List<RemoteKeysEntity>)

    @Query("SELECT * FROM remoteKeysEntity WHERE id = :id")
    fun getRemoteKeysId(id: String): RemoteKeysEntity?

    @Query("DELETE FROM remoteKeysEntity")
    fun deleteRemoteKeys()
}