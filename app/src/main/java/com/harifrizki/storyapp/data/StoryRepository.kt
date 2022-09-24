package com.harifrizki.storyapp.data

import androidx.lifecycle.LiveData
import com.harifrizki.storyapp.data.remote.RemoteDataSource
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.data.remote.response.LoginResponse
import com.harifrizki.storyapp.model.Login
import com.harifrizki.storyapp.model.Registration
import com.harifrizki.storyapp.utils.ApiResource
import com.harifrizki.storyapp.utils.AppExecutor
import com.harifrizki.storyapp.utils.DataResource

class StoryRepository(
    private val remote: RemoteDataSource,
    private val executor: AppExecutor
) : StoryDataSource {
    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            remoteData: RemoteDataSource,
            appExecutor: AppExecutor
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(remoteData, appExecutor)
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
}