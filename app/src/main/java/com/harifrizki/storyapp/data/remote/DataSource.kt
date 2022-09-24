package com.harifrizki.storyapp.data.remote

import androidx.lifecycle.LiveData
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.data.remote.response.LoginResponse
import com.harifrizki.storyapp.model.Login
import com.harifrizki.storyapp.model.Registration
import com.harifrizki.storyapp.utils.ApiResource

interface DataSource {
    fun registration(registration: Registration?):
            LiveData<ApiResource<GeneralResponse>>

    fun login(login: Login?):
            LiveData<ApiResource<LoginResponse>>
}