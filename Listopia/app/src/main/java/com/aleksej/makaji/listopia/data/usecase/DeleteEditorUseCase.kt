package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.UserRepository
import com.aleksej.makaji.listopia.data.usecase.value.DeleteEditorValue
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2019-11-24.
 */
class DeleteEditorUseCase @Inject constructor(private val mUserRepository: UserRepository) : UseCase<DeleteEditorValue, Unit>() {

    override suspend fun invoke(value: DeleteEditorValue): State<Unit> {
        return when (val deleteEditorRemote = mUserRepository.deleteEditorRemote(value)) {
            is SuccessState -> {
                mUserRepository.deleteEditor(value)
            }
            else -> deleteEditorRemote
        }
    }
}