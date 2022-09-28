package com.harifrizki.storyapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.harifrizki.storyapp.utils.EMPTY_STRING
import kotlinx.parcelize.Parcelize
import org.json.JSONObject
import retrofit2.Response

@Parcelize
data class GeneralResponse(
    @SerializedName("error") var error: Boolean? = false,
    @SerializedName("message") var message: String? = EMPTY_STRING
) : Parcelable

@Parcelize
data class LoginResultResponse(
    @SerializedName("userId") var userId: String? = EMPTY_STRING,
    @SerializedName("name") var name: String? = EMPTY_STRING,
    @SerializedName("token") var token: String? = EMPTY_STRING
) : Parcelable

data class LoginResponse(
    @SerializedName("error") var error: Boolean? = false,
    @SerializedName("message") var message: String? = EMPTY_STRING,
    @SerializedName("loginResult") var loginResult: LoginResultResponse? = LoginResultResponse()
)