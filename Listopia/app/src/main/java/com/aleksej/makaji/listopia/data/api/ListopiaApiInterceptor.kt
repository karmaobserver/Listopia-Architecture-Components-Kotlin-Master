package com.aleksej.makaji.listopia.data.api

import com.aleksej.makaji.listopia.util.SharedPreferenceManager
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 4/28/19.
 */
class ListopiaApiInterceptor  @Inject constructor(private val mSharedPreferenceManager: SharedPreferenceManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        request = request.newBuilder()
                .addHeader("Application", "LISTOPIA")
                .addHeader("Accept-Language", mSharedPreferenceManager.languageCode)
                .addHeader("Technology", "ANDROID")
                .header("Authorization", "Bearer ${mSharedPreferenceManager.token}")
                .addHeader("Content-Type", "application/json").build()

        val response = chain.proceed(request)

        val responseBodyString = response.body()?.string()

        return response.newBuilder().body(
                ResponseBody.create(response.body()?.contentType(),
                        responseBodyString.orEmpty())).build()
    }
}