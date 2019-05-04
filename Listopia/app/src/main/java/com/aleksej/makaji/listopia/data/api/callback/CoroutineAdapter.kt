package com.aleksej.makaji.listopia.data.api.callback

import com.aleksej.makaji.listopia.data.api.callback.CoroutineConverterUtil.convert
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.mapper.IDtoModelMapper
import com.aleksej.makaji.listopia.error.ErrorParser
import com.aleksej.makaji.listopia.error.ErrorResponse
import com.aleksej.makaji.listopia.error.ExceptionError
import com.aleksej.makaji.listopia.error.UnauthorizedError
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 4/28/19.
 */
class CoroutineAdapter<T : IDtoModelMapper<T, F>, F> @Inject constructor(private val response: Response<T>, private val retrofit: Retrofit) {

    operator fun invoke(): State<F> {
        if (response.code() == 401) {
            return State.Error(UnauthorizedError)
        }
        return if (response.isSuccessful) {
            val responseBody = response.body()
            State.Success(if (responseBody != null) response.body()?.map(responseBody) else null)
        } else {
            return try {
                val errorResponse = convert(ErrorConverter(retrofit), response.errorBody()) as ErrorResponse
                State.Error(ErrorParser.parseBackendError(errorResponse))
            } catch (e: Exception) {
                State.Error(ExceptionError(e))
            }
        }
    }
}