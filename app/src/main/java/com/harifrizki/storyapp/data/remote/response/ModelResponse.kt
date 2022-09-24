package com.harifrizki.storyapp.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.harifrizki.storyapp.utils.EMPTY_STRING
import kotlinx.parcelize.Parcelize
import org.json.JSONObject
import retrofit2.Response

@Parcelize
data class GeneralResponse(
    @SerializedName("error")   var error : Boolean? = false,
    @SerializedName("message") var message : String? = EMPTY_STRING
) : Parcelable {

}

@Parcelize
data class LoginResultResponse(
    @SerializedName("userId") var userId: String? = EMPTY_STRING,
    @SerializedName("name")   var name: String? = EMPTY_STRING,
    @SerializedName("token")  var token: String? = EMPTY_STRING
) : Parcelable

data class LoginResponse(
    @SerializedName("error")   var error : String? = EMPTY_STRING,
    @SerializedName("message") var message : String? = EMPTY_STRING,
    @SerializedName("loginResult") var loginResult: LoginResultResponse? = LoginResultResponse()
)

//@Parcelize
//data class ErrorResponse(
//    var errorCode: String? = null,
//    var errorMessage: String? = null,
//    var errorUrl: String? = null,
//    var errorTime: String? = null,
//    var errorThrow: String? = null
//) : Parcelable {
//    companion object {
//        fun errorResponse(throwable: Throwable): ErrorResponse {
//            return ErrorResponse().apply {
//                errorMessage = throwable.message
//                errorThrow = throwable.printStackTrace().toString()
//            }
//        }
//
//        fun <T> errorResponse(response: Response<T>, jsonError: JSONObject): ErrorResponse {
//            return ErrorResponse().apply {
//                errorUrl = response.raw().networkResponse?.request?.url?.toUrl().toString()
//                errorCode = response.raw().code.toString()
//                errorTime = jsonError.getString("timestamp")
//                errorMessage = jsonError.getString("error")
//            }
//        }
//    }
//}