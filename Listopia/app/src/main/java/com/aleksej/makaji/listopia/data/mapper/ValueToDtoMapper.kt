package com.aleksej.makaji.listopia.data.mapper

import com.aleksej.makaji.listopia.data.api.dto.request.SaveUserRequest
import com.aleksej.makaji.listopia.data.usecase.value.SaveUserValue
import com.aleksej.makaji.listopia.util.mapTo

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
fun SaveUserValue.mapToSaveUserRequest(): SaveUserRequest = mapTo<SaveUserRequest>().copy()