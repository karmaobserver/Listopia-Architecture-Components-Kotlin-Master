package com.aleksej.makaji.listopia.data.api.dto.response

import com.aleksej.makaji.listopia.data.mapper.IDtoModelMapper
import com.aleksej.makaji.listopia.data.mapper.mapToUserModel
import com.aleksej.makaji.listopia.data.repository.model.UserModel

/**
 * Created by Aleksej Makaji on 5/5/19.
 */
data class UserResponse(val id: Long,
                        var name: String,
                        val email: String,
                        val avatar: String,
                        val friends: List<UserResponse>? = null) : IDtoModelMapper<UserResponse, UserModel> {
    override fun map(value: UserResponse): UserModel {
        return value.mapToUserModel()
    }
}