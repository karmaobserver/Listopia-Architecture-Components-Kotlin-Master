package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.LoadingState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.UserRepository
import com.aleksej.makaji.listopia.data.usecase.value.SaveEditorValue
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2019-11-24.
 */
class SaveEditorUseCase @Inject constructor(private val mUserRepository: UserRepository): UseCase<SaveEditorValue, Long>() {

    override suspend fun invoke(value: SaveEditorValue): State<Long> {
        return when (val saveEditorRemote =  mUserRepository.saveEditorRemote(value)) {
            is SuccessState -> {
                mUserRepository.saveEditor(value)
            }
            is ErrorState -> {
                Timber.d("Failed to saveEditorRemote")
                ErrorState(saveEditorRemote.error)
            }
            is LoadingState -> LoadingState()
        }
    }
}