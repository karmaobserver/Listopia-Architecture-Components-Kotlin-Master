package com.aleksej.makaji.listopia.data.usecase

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
interface UseCase<T, F> {
    fun invoke(value: T): F
}