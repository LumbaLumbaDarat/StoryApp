package com.harifrizki.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.data.remote.response.GetAllStoriesResponse
import com.harifrizki.storyapp.data.remote.response.LoginResponse
import com.harifrizki.storyapp.data.remote.response.LoginResultResponse
import com.harifrizki.storyapp.database.StoryDatabase
import com.harifrizki.storyapp.model.Login
import com.harifrizki.storyapp.model.Registration
import com.harifrizki.storyapp.model.Story
import com.harifrizki.storyapp.model.StoryEntity
import com.harifrizki.storyapp.utils.DataResource

interface StoryDataSource {
    fun registration(registration: Registration?): LiveData<DataResource<GeneralResponse>>
    fun login(login: Login?): LiveData<DataResource<LoginResponse>>
    fun getAllStories(loginResultResponse: LoginResultResponse?, story: Story?):
            LiveData<DataResource<GetAllStoriesResponse>>
    fun getAllStoriesWithPaging(loginResultResponse: LoginResultResponse?, story: Story?):
            LiveData<PagingData<StoryEntity>>
    fun addStory(
        loginResultResponse: LoginResultResponse?,
        story: Story?
    ): LiveData<DataResource<GeneralResponse>>
}