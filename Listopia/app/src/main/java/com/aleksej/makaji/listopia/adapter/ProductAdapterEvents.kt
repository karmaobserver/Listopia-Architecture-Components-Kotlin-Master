package com.aleksej.makaji.listopia.adapter

import android.view.View
import com.aleksej.makaji.listopia.data.repository.model.ProductModel

/**
 * Created by Aleksej Makaji on 2/3/19.
 */
sealed class ProductAdapterEvents {
    data class ProductClick(val productModel: ProductModel) : ProductAdapterEvents()
    data class EditClick(val view: View, val productId: Long) : ProductAdapterEvents()
}