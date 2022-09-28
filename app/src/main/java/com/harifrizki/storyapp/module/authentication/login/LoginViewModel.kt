package com.harifrizki.storyapp.module.authentication.login

import androidx.lifecycle.LiveData
import com.harifrizki.storyapp.data.StoryRepository
import com.harifrizki.storyapp.data.remote.response.LoginResponse
import com.harifrizki.storyapp.model.Login
import com.harifrizki.storyapp.utils.BaseViewModel
import com.harifrizki.storyapp.utils.DataResource

class LoginViewModel (private val storyRepository: StoryRepository) : BaseViewModel() {
    fun login(login: Login?): LiveData<DataResource<LoginResponse>> =
        storyRepository.login(login)
}