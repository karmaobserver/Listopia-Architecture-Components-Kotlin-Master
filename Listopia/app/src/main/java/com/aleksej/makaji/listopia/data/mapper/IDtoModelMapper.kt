package com.aleksej.makaji.listopia.data.mapper

/**
 * Created by Aleksej Makaji on 4/28/19.
 */
interface IDtoModelMapper<T,F> {
    fun map(value: T): F
}