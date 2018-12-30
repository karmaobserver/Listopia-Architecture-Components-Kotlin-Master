package com.aleksej.makaji.listopia.data.event

import com.aleksej.makaji.listopia.error.ListopiaError

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
sealed class Event<T>
data class SuccessEvent<T>(val data: T?) : Event<T>()
class LoadingEvent<T> : Event<T>()
data class ErrorEvent<T>(val error: ListopiaError): Event<T>()