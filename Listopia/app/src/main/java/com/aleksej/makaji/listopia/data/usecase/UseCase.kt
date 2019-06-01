package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.State

/**
 * Created by Aleksej Makaji on 2019-06-01.
 */
abstract class UseCase<in Value, Type> {

    abstract suspend fun invoke(value: Value): State<Type>
}