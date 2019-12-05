package com.aleksej.makaji.listopia.data.api.dto.request

import java.util.*

/**
 * Created by Aleksej Makaji on 2019-11-24.
 */
data class DeleteEditorRequest(val editorId: String, val shoppingListId: String, val timestamp: Date)