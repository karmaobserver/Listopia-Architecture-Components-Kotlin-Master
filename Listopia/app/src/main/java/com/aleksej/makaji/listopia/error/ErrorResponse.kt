package com.aleksej.makaji.listopia.error

/**
 * Created by Aleksej Makaji on 12/30/18.
 */
data class ErrorResponse(
        var code: Int,
        var message: String,
        var data: String?
        //val data: ErrorData?
)

/*
data class ErrorData(var dialogType: String?,
                     var info: String?)*/
