package com.harifrizki.storyapp.module.authentication.registration

import androidx.lifecycle.LiveData
import com.harifrizki.storyapp.data.StoryRepository
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.model.Registration
import com.harifrizki.storyapp.utils.BaseViewModel
import com.harifrizki.storyapp.utils.DataResource

class RegistrationViewModel (private val storyRepository: StoryRepository) : BaseViewModel() {
    fun registration(registration: Registration?): LiveData<DataResource<GeneralResponse>> =
        storyRepository.registration(registration)
}