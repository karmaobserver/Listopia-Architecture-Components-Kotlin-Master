package com.aleksej.makaji.listopia.util

import com.aleksej.makaji.listopia.R
import com.aleksej.makaji.listopia.error.EmailError
import com.aleksej.makaji.listopia.error.ListNameError
import com.aleksej.makaji.listopia.error.ProductNameError
import java.util.regex.Pattern

/**
 * Created by Aleksej Makaji on 1/26/19.
 */
object Validator {

    private const val PASSWORD_PATTERN = "^(?!.*(\\/|\\|\"|;|,|:))(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%&?+-=*<>.]{8,}$"
    private const val EMAIL_PATTERN = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"

    fun validateListName(listName: String): ListNameError? {
        if(listName.trim().isEmpty()) return ListNameError(R.string.error_list_name)
        return null
    }

    fun validateProductName(productName: String): ProductNameError? {
        if(productName.trim().isEmpty()) return ProductNameError(R.string.error_product_name)
        return null
    }

    fun validateEmail(email: String): EmailError? {
        if(email.trim().isEmpty()) return EmailError(R.string.error_email_is_empty)
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        if (!matcher.matches()) return EmailError(R.string.error_email_is_invalid)
        return null
    }
}