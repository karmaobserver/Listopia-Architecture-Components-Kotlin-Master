package com.aleksej.makaji.listopia.data.usecase

import com.aleksej.makaji.listopia.data.event.ErrorState
import com.aleksej.makaji.listopia.data.event.LoadingState
import com.aleksej.makaji.listopia.data.event.State
import com.aleksej.makaji.listopia.data.event.SuccessState
import com.aleksej.makaji.listopia.data.repository.UserRepository
import com.aleksej.makaji.listopia.data.usecase.value.FetchAndSaveFriendsValue
import javax.inject.Inject

/**
 * Created by Aleksej Makaji on 2019-12-01.
 */
class FetchAndSaveFriendsUseCase @Inject constructor(private val mUserRepository: UserRepository) : UseCase<FetchAndSaveFriendsValue, Unit>() {
    override suspend fun invoke(value: FetchAndSaveFriendsValue): State<Unit> {
        when (val friendsRemote = mUserRepository.fetchFriends(value.friendsId)) {
            is SuccessState -> {
                friendsRemote.data?.let {
                    return when (val saveFriendsResponse = mUserRepository.saveUsers(it)) {
                        is SuccessState -> SuccessState(Unit)
                        is LoadingState -> LoadingState()
                        is ErrorState -> ErrorState(saveFriendsResponse.error)

                    }
                }
            }
            is LoadingState -> return LoadingState()
            is ErrorState -> return ErrorState(friendsRemote.error)
        }
        //No friends
        return SuccessState(Unit)
    }
    /*override suspend fun invoke(value: FetchAndSaveFriendsValue): State<Unit> {
        when (val friendsRoom = mUserRepository.getUserByIdSuspended(mSharedPreferenceManager.userId)) {
            is SuccessState -> {
                friendsRoom.data?.let {
                    val friendsId = arrayListOf<String>()
                    it.friends?.forEach {
                        friendsId.add(it.id)
                    }
                    when (val friendsRemote = mUserRepository.fetchFriends(friendsId)) {
                        is SuccessState -> {
                            friendsRemote.data?.let {
                                return when (val saveFriendsResponse = mUserRepository.saveUsers(it)) {
                                    is SuccessState -> SuccessState(Unit)
                                    is LoadingState -> LoadingState()
                                    is ErrorState -> ErrorState(saveFriendsResponse.error)

                                }
                            }
                        }
                        is LoadingState -> return LoadingState()
                        is ErrorState -> return ErrorState(friendsRemote.error)
                    }
                }
            }
            is LoadingState -> return LoadingState()
            is ErrorState -> return ErrorState(friendsRoom.error)
        }
        //No friends
        return SuccessState(Unit)
    }*/
}