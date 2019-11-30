package com.aleksej.makaji.listopia.data.api.dto.response

import com.aleksej.makaji.listopia.data.mapper.IDtoModelMapper
import com.aleksej.makaji.listopia.data.mapper.mapToProductModel
import com.aleksej.makaji.listopia.data.repository.model.ProductModel

/**
 * Created by Aleksej Makaji on 2019-11-30.
 */
class ProductsResponse : ArrayList<ProductResponse>(), IDtoModelMapper<ProductsResponse, List<ProductModel>> {
    override fun map(value: ProductsResponse): List<ProductModel> {
        return value.map { it.mapToProductModel() }
    }
}