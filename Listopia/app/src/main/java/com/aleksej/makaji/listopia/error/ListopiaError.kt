package com.aleksej.makaji.listopia.error

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
sealed class ListopiaError
//GLOBAL ERROR
object UnknownError : ListopiaError()
data class ExceptionError(val exception: Exception) : ListopiaError()
//DATABASE ERROR
data class RoomError(val exception: Exception): ListopiaError()
//API ERROR
data class ThrowableError(val throwable: Throwable) : ListopiaError()
data class BackendError(val response: ErrorResponse) : ListopiaError()
object UnauthorizedError : ListopiaError()
object NoRemoteData : ListopiaError()

//Specific errors
data class UserDoesNotExistsError(val response: ErrorResponse): ListopiaError()

//View Errors
data class ListNameError(val resourceId : Int) : ListopiaError()
data class ProductNameError(val resourceId : Int) : ListopiaError()
data class EmailError(val resourceId : Int) : ListopiaError()

const val ERROR_NOT_FOUND = 404