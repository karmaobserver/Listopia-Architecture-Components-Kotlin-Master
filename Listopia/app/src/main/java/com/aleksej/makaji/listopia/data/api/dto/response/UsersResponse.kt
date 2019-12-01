package com.aleksej.makaji.listopia.data.api.dto.response

import com.aleksej.makaji.listopia.data.mapper.IDtoModelMapper
import com.aleksej.makaji.listopia.data.mapper.mapToUserModel
import com.aleksej.makaji.listopia.data.repository.model.UserModel

/**
 * Created by Aleksej Makaji on 2019-12-01.
 */
class UsersResponse : ArrayList<UserResponse>(), IDtoModelMapper<UsersResponse, List<UserModel>> {
    override fun map(value: UsersResponse): List<UserModel> {
        return value.map { it.mapToUserModel() }
    }
}