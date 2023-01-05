package az.needforspeak.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.needforspeak.base.BaseActivity
import az.needforspeak.model.remote.auth.ProfileResponseModel
import az.needforspeak.model.remote.auth.UserSearchResponse
import az.needforspeak.repository.FriendsRepository
import az.needforspeak.repository.MainRepository
import az.needforspeak.utils.MyAccount
import az.needforspeak.utils.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FriendsViewModel(private val friendsRepository: FriendsRepository): ViewModel() {

    val showError = MutableLiveData<Boolean>()
    val showSuccess = MutableLiveData<Boolean>()

    private val _userSearchLiveData: MutableLiveData<NetworkResult<UserSearchResponse>> = MutableLiveData()
    val userSearchLiveData: LiveData<NetworkResult<UserSearchResponse>> = _userSearchLiveData

    fun sendFriendRequest(userJid: String){
        BaseActivity.loadingUp()
        viewModelScope.launch(Dispatchers.IO) {
            BaseActivity.loadingDown()
            friendsRepository.sendFriendRequest(userJid)
        }
    }

    fun searchUser(userId: String) = viewModelScope.launch {
        BaseActivity.loadingUp()
        friendsRepository.searchUser(userId).collect { values ->
            _userSearchLiveData.value = values
        }
    }

    /* fun searchUserXMPP(userName: String): MutableLiveData<ArrayList<MyAccount>> {
       val searchResult = MutableLiveData<ArrayList<MyAccount>>()
       BaseActivity.loadingUp()
       viewModelScope.launch(Dispatchers.IO) {
           *//*  mainRepository.searchUserXMPP(userName) { result ->
                  result
                  searchResult.postValue(result)
                  BaseActivity.loadingDown()
              }*//*

        }
        return searchResult
    }*/

}