package com.harifrizki.storyapp.module.story.addstory

import androidx.lifecycle.LiveData
import com.harifrizki.storyapp.data.StoryRepository
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.data.remote.response.LoginResultResponse
import com.harifrizki.storyapp.model.Story
import com.harifrizki.storyapp.utils.BaseViewModel
import com.harifrizki.storyapp.utils.DataResource

class AddStoryViewModel(private val storyRepository: StoryRepository) : BaseViewModel() {
    fun addStory(loginResultResponse: LoginResultResponse, story: Story): LiveData<DataResource<GeneralResponse>> =
        storyRepository.addStory(loginResultResponse, story)
}