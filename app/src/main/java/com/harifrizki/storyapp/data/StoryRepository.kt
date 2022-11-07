package com.harifrizki.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.harifrizki.storyapp.data.remote.RemoteDataSource
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.data.remote.response.GetAllStoriesResponse
import com.harifrizki.storyapp.data.remote.response.LoginResponse
import com.harifrizki.storyapp.data.remote.response.LoginResultResponse
import com.harifrizki.storyapp.database.StoryDatabase
import com.harifrizki.storyapp.model.Login
import com.harifrizki.storyapp.model.Registration
import com.harifrizki.storyapp.model.Story
import com.harifrizki.storyapp.model.StoryEntity
import com.harifrizki.storyapp.utils.ApiResource
import com.harifrizki.storyapp.utils.AppExecutor
import com.harifrizki.storyapp.utils.DataResource

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val remote: RemoteDataSource,
    private val executor: AppExecutor
) : StoryDataSource {
    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            storyDatabase: StoryDatabase,
            remoteData: RemoteDataSource,
            appExecutor: AppExecutor
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(storyDatabase, remoteData, appExecutor)
            }
    }

    override fun registration(registration: Registration?): LiveData<DataResource<GeneralResponse>> {
        return object : NetworkResource<GeneralResponse>() {
            override fun createCall(): LiveData<ApiResource<GeneralResponse>> =
                remote.registration(registration)
        }.asLiveData()
    }

    override fun login(login: Login?): LiveData<DataResource<LoginResponse>> {
        return object : NetworkResource<LoginResponse>() {
            override fun createCall(): LiveData<ApiResource<LoginResponse>> =
                remote.login(login)
        }.asLiveData()
    }

    override fun getAllStories(loginResultResponse: LoginResultResponse?, story: Story?): LiveData<DataResource<GetAllStoriesResponse>> {
        return object : NetworkResource<GetAllStoriesResponse>() {
            override fun createCall(): LiveData<ApiResource<GetAllStoriesResponse>> =
                remote.getAllStories(loginResultResponse, story)
        }.asLiveData()
    }

    override fun getAllStoriesWithPaging(
        loginResultResponse: LoginResultResponse?,
        story: Story?
    ): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator = loginResultResponse?.let { StoryRemoteMediator(storyDatabase, it) },
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    override fun addStory(
        loginResultResponse: LoginResultResponse?,
        story: Story?
    ): LiveData<DataResource<GeneralResponse>> {
        return object : NetworkResource<GeneralResponse>() {
            override fun createCall(): LiveData<ApiResource<GeneralResponse>> =
                remote.addStory(loginResultResponse, story)
        }.asLiveData()
    }
}