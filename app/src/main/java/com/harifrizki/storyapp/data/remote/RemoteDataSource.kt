package com.harifrizki.storyapp.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.data.remote.response.LoginResponse
import com.harifrizki.storyapp.model.Login
import com.harifrizki.storyapp.model.Registration
import com.harifrizki.storyapp.utils.ApiResource
import com.harifrizki.storyapp.utils.ResponseStatus
import com.harifrizki.storyapp.utils.ResponseStatus.*
import com.harifrizki.storyapp.utils.ZERO
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response

class RemoteDataSource : DataSource {
    companion object {
        private val TAG: String =
            RemoteDataSource::javaClass.name
        private var INSTANCE: RemoteDataSource? = null

        fun getInstance(): RemoteDataSource =
            INSTANCE ?: RemoteDataSource()
    }

    init {
        Logger.addLogAdapter(AndroidLogAdapter())
    }

    override fun registration(registration: Registration?): LiveData<ApiResource<GeneralResponse>> {
        return response(
            NetworkApi.connectToApi().registration(Registration.jsonObject(registration!!)),
            GeneralResponse()
        )
    }

    override fun login(login: Login?): LiveData<ApiResource<LoginResponse>> {
        return response(NetworkApi.connectToApi().login(Login.jsonObject(login!!)), LoginResponse())
    }

    private fun <T> response(client: Call<T>, modelResponse: T):
            MutableLiveData<ApiResource<T>> {
        val result = MutableLiveData<ApiResource<T>>()
        try {
            client.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    convertResponse(response, modelResponse, result)
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    convertResponse(modelResponse, result, t, ERROR)
                }
            })
        } catch (e: Exception) {
            convertResponse(modelResponse, result, e, EMPTY)
        }
        return result
    }

    private fun <T> convertResponse(
        response: Response<T>,
        modelResponse: T,
        result: MutableLiveData<ApiResource<T>>
    ) {
        if (response.isSuccessful)
            result.value = ApiResource.success(response.body()!!)
        else result.value = ApiResource.error(
            modelResponse,
            NetworkApi.error(response)
        )
    }

    private fun <T> convertResponse(
        modelResponse: T,
        result: MutableLiveData<ApiResource<T>>,
        throwable: Throwable,
        responseStatus: ResponseStatus
    ) {
        Logger.e(throwable.message.toString());
        when (responseStatus) {
            ERROR -> {
                result.value =
                    ApiResource.error(modelResponse, GeneralResponse(true, throwable.message))
            }
            else -> {
                result.value =
                    ApiResource.empty(modelResponse, GeneralResponse(true, throwable.message))
            }
        }
    }
}