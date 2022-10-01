package com.harifrizki.storyapp.data.remote

import com.google.gson.JsonObject
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.data.remote.response.GetAllStoriesResponse
import com.harifrizki.storyapp.data.remote.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface EndPoint {
    @Headers("Content-Type: application/json")
    @POST(REGISTRATION)
    fun registration(@Body jsonObject: JsonObject):
            Call<GeneralResponse>

    @Headers("Content-Type: application/json")
    @POST(LOGIN)
    fun login(@Body jsonObject: JsonObject):
            Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @GET(GET_ALL_STORIES)
    fun getAllStories():
            Call<GetAllStoriesResponse>

    @Headers("Content-Type: application/json")
    @POST(ADD_STORY)
    fun addStory():
            Call<GeneralResponse>
}