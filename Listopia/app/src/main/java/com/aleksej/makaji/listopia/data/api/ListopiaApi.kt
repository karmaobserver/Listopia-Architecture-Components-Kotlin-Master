package com.aleksej.makaji.listopia.data.api

import com.aleksej.makaji.listopia.data.api.dto.request.SaveFriendRequest
import com.aleksej.makaji.listopia.data.api.dto.request.SaveShoppingListRequest
import com.aleksej.makaji.listopia.data.api.dto.request.SaveUserRequest
import com.aleksej.makaji.listopia.data.api.dto.request.UpdateShoppingListRequest
import com.aleksej.makaji.listopia.data.api.dto.response.EmptyResponse
import com.aleksej.makaji.listopia.data.api.dto.response.ShoppingListsResponse
import com.aleksej.makaji.listopia.data.api.dto.response.UserResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Aleksej Makaji on 4/28/19.
 */
interface ListopiaApi {

    @GET("hello")
    suspend fun testCloudFunction(): Response<EmptyResponse>

    @POST("user/save")
    suspend fun saveUser(@Body saveUserRequest: SaveUserRequest): Response<EmptyResponse>

    @GET("user/get/{userId}")
    suspend fun fetchUserById(@Path("userId") userId: String): Response<UserResponse>

    @POST("user/{userId}/add-friend")
    suspend fun saveFriend(@Path("userId") userId: String,
                           @Body saveFriendRequest: SaveFriendRequest): Response<EmptyResponse>

    @POST("shopping-list/add")
    suspend fun saveShoppingList(@Body saveShoppingListRequest: SaveShoppingListRequest): Response<EmptyResponse>

    @PUT("shopping-list/update")
    suspend fun updateShoppingList(@Body updateShoppingListRequest: UpdateShoppingListRequest) : Response<EmptyResponse>

    @GET("shopping-list/{userId}")
    suspend fun fetchShoppingListsByUserId(@Path("userId") userId: String): Response<ShoppingListsResponse>

}