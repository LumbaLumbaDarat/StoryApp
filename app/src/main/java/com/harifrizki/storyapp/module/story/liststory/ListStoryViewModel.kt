package com.harifrizki.storyapp.module.story.liststory

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.harifrizki.storyapp.data.StoryRepository
import com.harifrizki.storyapp.data.remote.response.GetAllStoriesResponse
import com.harifrizki.storyapp.data.remote.response.LoginResultResponse
import com.harifrizki.storyapp.model.Story
import com.harifrizki.storyapp.model.StoryEntity
import com.harifrizki.storyapp.utils.BaseViewModel
import com.harifrizki.storyapp.utils.DataResource

class ListStoryViewModel(private val storyRepository: StoryRepository) : BaseViewModel() {
    fun story(loginResultResponse: LoginResultResponse, story: Story): LiveData<DataResource<GetAllStoriesResponse>> =
        storyRepository.getAllStories(loginResultResponse, story)
    fun storyWithPaging(loginResultResponse: LoginResultResponse, story: Story): LiveData<PagingData<StoryEntity>> =
        storyRepository.getAllStoriesWithPaging(loginResultResponse, story)
}