package com.aleksej.makaji.listopia.error

/**
 * Created by Aleksej Makaji on 4/28/19.
 */
object ErrorParser {
    fun parseBackendError(errorResponse: ErrorResponse) : ListopiaError {
        return BackendError(errorResponse)
    }
}