package com.harifrizki.storyapp.module.story.mapsstory

import androidx.lifecycle.LiveData
import com.harifrizki.storyapp.data.StoryRepository
import com.harifrizki.storyapp.data.remote.response.GetAllStoriesResponse
import com.harifrizki.storyapp.data.remote.response.LoginResultResponse
import com.harifrizki.storyapp.model.Story
import com.harifrizki.storyapp.utils.BaseViewModel
import com.harifrizki.storyapp.utils.DataResource

class StoryMapsViewModel(private val storyRepository: StoryRepository) : BaseViewModel() {
    fun story(loginResultResponse: LoginResultResponse, story: Story): LiveData<DataResource<GetAllStoriesResponse>> =
        storyRepository.getAllStories(loginResultResponse, story)
}