package com.harifrizki.storyapp.utils

import com.harifrizki.storyapp.data.StoryRepository
import com.harifrizki.storyapp.data.remote.RemoteDataSource

object Injection {
    fun provideRepository(): StoryRepository {
        return StoryRepository.getInstance(
            RemoteDataSource.getInstance(),
            AppExecutor())
    }
}