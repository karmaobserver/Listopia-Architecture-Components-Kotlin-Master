package com.aleksej.makaji.listopia.data.api.callback

import com.aleksej.makaji.listopia.error.ErrorResponse
import okhttp3.ResponseBody
import retrofit2.Converter

/**
 * Created by Aleksej Makaji on 4/28/19.
 */
object CoroutineConverterUtil {
    fun convert(converter: Converter<ResponseBody, ErrorResponse>, body: ResponseBody?): Any? {
        if (body == null || body.contentLength() == 0L) {
            return null
        }
        return try {
            converter.convert(body)
        } catch (e: Exception) {
            throw e
        }
    }
}