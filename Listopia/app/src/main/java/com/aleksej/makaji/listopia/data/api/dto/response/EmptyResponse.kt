package com.aleksej.makaji.listopia.data.api.dto.response

import com.aleksej.makaji.listopia.data.mapper.IDtoModelMapper

/**
 * Created by Aleksej Makaji on 4/28/19.
 */
class EmptyResponse : IDtoModelMapper<EmptyResponse, Unit> {
    override fun map(value: EmptyResponse) {}
}