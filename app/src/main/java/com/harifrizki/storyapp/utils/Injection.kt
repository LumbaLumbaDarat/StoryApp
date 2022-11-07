package com.harifrizki.storyapp.utils

import android.content.Context
import com.harifrizki.storyapp.data.StoryRepository
import com.harifrizki.storyapp.data.remote.RemoteDataSource
import com.harifrizki.storyapp.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        return StoryRepository.getInstance(
            StoryDatabase.getDatabase(context),
            RemoteDataSource.getInstance(),
            AppExecutor())
    }
}