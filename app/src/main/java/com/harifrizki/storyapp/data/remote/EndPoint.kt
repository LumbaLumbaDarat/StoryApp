package com.harifrizki.storyapp.data.remote

import com.google.gson.JsonObject
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.data.remote.response.GetAllStoriesResponse
import com.harifrizki.storyapp.data.remote.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

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
    fun getAllStories(@Body jsonObject: JsonObject):
            Call<GetAllStoriesResponse>

    @Headers("Content-Type: application/json")
    @GET(GET_ALL_STORIES)
    @Multipart
    fun getAllStoriesWitPaging(@Part page: Int, @Part size: Int, @Part location: Int):
            Call<GetAllStoriesResponse>

    @Multipart
    @POST(ADD_STORY)
    fun addStory(@Part("description") description: RequestBody,
                 @Part photo: MultipartBody.Part):
            Call<GeneralResponse>
}