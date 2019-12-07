package com.aleksej.makaji.listopia.data.api

import com.aleksej.makaji.listopia.data.api.dto.request.*
import com.aleksej.makaji.listopia.data.api.dto.response.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Aleksej Makaji on 4/28/19.
 */
interface ListopiaApi {

    @POST("user/save")
    suspend fun saveUser(@Body saveUserRequest: SaveUserRequest): Response<EmptyResponse>

    @GET("user/get/{userId}")
    suspend fun fetchUserById(@Path("userId") userId: String): Response<UserResponse>

    @POST("user/{userId}/add-friend")
    suspend fun saveFriend(@Path("userId") userId: String,
                           @Body saveFriendRequest: SaveFriendRequest): Response<UserResponse>

    @DELETE("user/{userId}/delete-friend/{friendId}")
    suspend fun deleteFriendById(@Path("userId") userId: String,
                                 @Path("friendId") friendId: String): Response<EmptyResponse>

    @POST("shopping-list/add")
    suspend fun saveShoppingList(@Body saveShoppingListRequest: SaveShoppingListRequest): Response<EmptyResponse>

    @PUT("shopping-list/update")
    suspend fun updateShoppingList(@Body updateShoppingListRequest: UpdateShoppingListRequest) : Response<EmptyResponse>

    @GET("shopping-list/get-all/{userId}")
    suspend fun fetchShoppingListsByUserId(@Path("userId") userId: String): Response<ShoppingListsResponse>

    @GET("shopping-list/get/{shoppingListId}")
    suspend fun fetchShoppingListById(@Path("shoppingListId") shoppingListId: String): Response<ShoppingListResponse>

    @POST("shopping-list/add-editor")
    suspend fun saveEditor(@Body saveEditorRequest: SaveEditorRequest): Response<EmptyResponse>

    @PUT("shopping-list/delete-editor")
    suspend fun deleteEditor(@Body deleteEditorRequest: DeleteEditorRequest): Response<EmptyResponse>

    @POST("product/add")
    suspend fun saveProduct(@Body saveProductRequest: SaveProductRequest): Response<EmptyResponse>

    @POST("products")
    suspend fun fetchProducts(@Body fetchProductsRequest: FetchProductsRequest): Response<ProductsResponse>

    @GET("shopping-list/{shoppingListId}/product/{productId}")
    suspend fun fetchProductById( @Path("shoppingListId") shoppingListId: String,
                                  @Path("productId") productId: String): Response<ProductResponse>

    @PUT("product/update")
    suspend fun updateProduct(@Body updateProductRequest: UpdateProductRequest) : Response<EmptyResponse>

    @DELETE("product/delete/{shoppingListId}/{productId}")
    suspend fun deleteProductById(@Path("productId") productId: String,
                                  @Path("shoppingListId") shoppingListId: String) : Response<EmptyResponse>

    @DELETE("shopping-list/delete/{shoppingListId}")
    suspend fun deleteShoppingListById(@Path("shoppingListId") shoppingListId: String) : Response<EmptyResponse>

    @POST("user/friends")
    suspend fun fetchFriends(@Body fetchFriendsRequest: FetchFriendsRequest): Response<UsersResponse>

    @PUT("user/firebase")
    suspend fun updateFirebaseToken(@Body updateFirebaseTokenRequest: UpdateFirebaseTokenRequest) : Response<EmptyResponse>
}