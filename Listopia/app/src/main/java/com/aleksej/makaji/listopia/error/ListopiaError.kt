package com.aleksej.makaji.listopia.error

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
sealed class ListopiaError
//GLOBAL ERROR
object UnknownError : ListopiaError()
//DATABASE ERROR
object RoomError: ListopiaError()
object RoomDeletingError: ListopiaError()
//API ERROR
data class ThrowableError(val throwable: Throwable) : ListopiaError()
data class BackendError(val response: ErrorResponse) : ListopiaError()
object UnauthorizedError : ListopiaError()

//View Errors
data class ListNameError(val resourceId : Int) : ListopiaError()