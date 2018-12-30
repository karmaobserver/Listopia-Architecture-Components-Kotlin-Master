package com.aleksej.makaji.listopia.data.event

import com.aleksej.makaji.listopia.error.ListopiaError

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
data class EventHandler<out T>(val event: Event<out T>) {
    //Static methods in case we want different syntax (We will see on long road which is better)
    companion object {
        fun <T> success(data: T?): EventHandler<T> {
            return EventHandler(SuccessEvent(data))
        }

        fun <T> error(error: ListopiaError): EventHandler<T> {
            return EventHandler(ErrorEvent(error))
        }

        fun <T> loading(): EventHandler<T> {
            return EventHandler(LoadingEvent())
        }
    }

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): EventHandler<T?>? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            EventHandler(event)
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): EventHandler<T> = EventHandler(event)
}