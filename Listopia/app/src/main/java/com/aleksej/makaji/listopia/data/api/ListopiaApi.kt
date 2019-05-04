package com.aleksej.makaji.listopia.data.api

import com.aleksej.makaji.listopia.data.api.dto.response.EmptyResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Aleksej Makaji on 4/28/19.
 */
interface ListopiaApi {

    @GET("hello")
    suspend fun testCloudFunction(): Response<EmptyResponse>

}