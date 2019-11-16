package com.aleksej.makaji.listopia.error

/**
 * Created by Aleksej Makaji on 4/28/19.
 */
object ErrorParser {
    const val ERROR_USER_DOES_NOT_EXISTS = "USER_DOES_NOT_EXISTS"

    fun parseBackendError(errorResponse: ErrorResponse) : ListopiaError {
        return when (errorResponse.errorType) {
            ERROR_USER_DOES_NOT_EXISTS -> UserDoesNotExistsError(errorResponse)
            else -> BackendError(errorResponse)
        }
    }
}