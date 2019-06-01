package com.aleksej.makaji.listopia.data.event

import com.aleksej.makaji.listopia.error.ListopiaError

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
data class StateHandler<out T>(val state: State<out T>) {
    //Static methods in case we want different syntax (We will see on long road which is better)
    companion object {
        fun <T> success(data: T?): StateHandler<T> {
            return StateHandler(SuccessState(data))
        }

        fun <T> error(error: ListopiaError): StateHandler<T> {
            return StateHandler(ErrorState(error))
        }

        fun <T> loading(): StateHandler<T> {
            return StateHandler(LoadingState())
        }
    }

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): StateHandler<T?>? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            StateHandler(state)
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): StateHandler<T> = StateHandler(state)
}