package com.aleksej.makaji.listopia.util

import androidx.appcompat.app.AlertDialog
import com.aleksej.makaji.listopia.HomeActivity
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2019-11-16.
 */
class DialogController @Inject constructor(private val homeActivity: HomeActivity) {

    fun showDialog(title: String? = "", message: String? = "", positiveButtonName: String?, negativeButtonName: String?, positiveClick: () -> Unit, negativeClick: () -> Unit) {
        val builder = AlertDialog.Builder(homeActivity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setCancelable(false)
        positiveButtonName?.run {
            builder.setPositiveButton(positiveButtonName) { _, _ -> positiveClick.invoke() }
        }
        negativeButtonName?.run {
            builder.setNegativeButton(negativeButtonName) { _, _ -> negativeClick.invoke() }
        }
        builder.show()
    }
}