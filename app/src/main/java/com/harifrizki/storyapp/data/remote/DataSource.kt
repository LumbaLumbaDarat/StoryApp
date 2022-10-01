package com.harifrizki.storyapp.data.remote

import androidx.lifecycle.LiveData
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.data.remote.response.GetAllStoriesResponse
import com.harifrizki.storyapp.data.remote.response.LoginResponse
import com.harifrizki.storyapp.data.remote.response.LoginResultResponse
import com.harifrizki.storyapp.model.Login
import com.harifrizki.storyapp.model.Registration
import com.harifrizki.storyapp.model.Story
import com.harifrizki.storyapp.utils.ApiResource

interface DataSource {
    fun registration(registration: Registration?):
            LiveData<ApiResource<GeneralResponse>>

    fun login(login: Login?):
            LiveData<ApiResource<LoginResponse>>

    fun getAllStories(loginResultResponse: LoginResultResponse?):
            LiveData<ApiResource<GetAllStoriesResponse>>

    fun addStory(loginResultResponse: LoginResultResponse?, story: Story?):
            LiveData<ApiResource<GeneralResponse>>
}