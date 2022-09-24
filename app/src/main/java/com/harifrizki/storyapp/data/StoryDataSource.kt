package com.harifrizki.storyapp.data

import androidx.lifecycle.LiveData
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.data.remote.response.LoginResponse
import com.harifrizki.storyapp.model.Login
import com.harifrizki.storyapp.model.Registration
import com.harifrizki.storyapp.utils.DataResource

interface StoryDataSource {
    fun registration(registration: Registration?): LiveData<DataResource<GeneralResponse>>
    fun login(login: Login?): LiveData<DataResource<LoginResponse>>
}