package com.aleksej.makaji.listopia.data.event

import com.aleksej.makaji.listopia.error.ListopiaError

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
sealed class State<T>
data class SuccessState<T>(val data: T?) : State<T>()
class LoadingState<T> : State<T>()
data class ErrorState<T>(val error: ListopiaError): State<T>()
