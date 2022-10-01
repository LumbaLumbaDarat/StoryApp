package com.harifrizki.storyapp.data.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.utils.ZERO
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkApi {
    companion object {
        private fun retrofit(authenticationBearerToken: String?): Retrofit {
            val interceptor: HttpLoggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val client: OkHttpClient =
                OkHttpClient().newBuilder().addInterceptor(interceptor)
                    .addInterceptor(AuthInterceptor(authenticationBearerToken))
                    .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS).build()

            val gson: Gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder().baseUrl(MAIN_URL_API)
                .addConverterFactory(GsonConverterFactory.create(gson)).client(client).build()
        }

        fun connectToApi(authenticationBearerToken: String?): EndPoint {
            return retrofit(authenticationBearerToken).create(EndPoint::class.java)
        }

        fun <T> error(authenticationBearerToken: String?, response: Response<T>): GeneralResponse {
            val converter: Converter<ResponseBody, GeneralResponse> =
                retrofit(authenticationBearerToken)
                    .responseBodyConverter(
                        GeneralResponse::class.java,
                        arrayOfNulls<Annotation>(ZERO)
                    )
            return try {
                response.errorBody()?.let { converter.convert(it) }!!
            } catch (e: Exception) {
                GeneralResponse(true, e.message)
            }
        }
    }
}

class AuthInterceptor(authenticationBearerToken: String?) : Interceptor {
    private val token = authenticationBearerToken
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("Authorization", "Bearer $token")
        return chain.proceed(requestBuilder.build())
    }
}