package com.harifrizki.storyapp.utils

import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.utils.ResponseStatus.*

class DataResource<T>(
    val responseStatus: ResponseStatus,
    val data: T?,
    val generalResponse: GeneralResponse?
) {
    companion object {
        fun <T> success(data: T?): DataResource<T> = DataResource(SUCCESS, data, null)
        fun <T> error(generalResponse: GeneralResponse??, data: T?): DataResource<T> =
            DataResource(ERROR, data, generalResponse)

        fun <T> loading(data: T?): DataResource<T> = DataResource(LOADING, data, null)
    }
}

class ApiResource<T>(
    val responseStatus: ResponseStatus,
    val body: T,
    val generalResponse: GeneralResponse??
) {
    companion object {
        fun <T> success(body: T): ApiResource<T> = ApiResource(SUCCESS, body, null)
        fun <T> empty(body: T, generalResponse: GeneralResponse?): ApiResource<T> =
            ApiResource(EMPTY, body, generalResponse)

        fun <T> error(body: T, generalResponse: GeneralResponse?): ApiResource<T> =
            ApiResource(ERROR, body, generalResponse)
    }
}