package com.aleksej.makaji.listopia.data.event

import com.aleksej.makaji.listopia.error.ListopiaError

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
sealed class State<T> {
    data class Success<T>(val data: T?) : State<T>()
    class Loading<T> : State<T>()
    data class Error<T>(val error: ListopiaError): State<T>()
}
