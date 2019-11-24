package com.aleksej.makaji.listopia.data.mapper

import com.aleksej.makaji.listopia.data.api.dto.request.DeleteEditorRequest
import com.aleksej.makaji.listopia.data.api.dto.request.SaveEditorRequest
import com.aleksej.makaji.listopia.data.api.dto.request.SaveFriendRequest
import com.aleksej.makaji.listopia.data.usecase.value.DeleteEditorValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveEditorValue
import com.aleksej.makaji.listopia.data.usecase.value.SaveFriendValue
import com.aleksej.makaji.listopia.util.mapTo

/**
 * Created by Aleksej Makaji on 5/4/19.
 */
fun SaveFriendValue.mapToSaveFriendRequest(): SaveFriendRequest = mapTo<SaveFriendRequest>().copy()

fun SaveEditorValue.mapToSaveEditorRequest(): SaveEditorRequest = mapTo<SaveEditorRequest>().copy()

fun DeleteEditorValue.mapToDeleteEditorRequest(): DeleteEditorRequest = mapTo<DeleteEditorRequest>().copy()