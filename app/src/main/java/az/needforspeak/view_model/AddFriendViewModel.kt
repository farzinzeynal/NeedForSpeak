package az.needforspeak.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.needforspeak.base.BaseActivity
import az.needforspeak.repository.AuthRepositry
import az.needforspeak.repository.MainRepository
import az.needforspeak.utils.MyAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddFriendViewModel(private val mainRepository: MainRepository): ViewModel() {
    val showError = MutableLiveData<Boolean>()
    val successLogin = MutableLiveData<Boolean>()

    fun searchUser(userName: String): MutableLiveData<ArrayList<MyAccount>> {
        val searchResult = MutableLiveData<ArrayList<MyAccount>>()
        BaseActivity.loadingDown()
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.searchUser(userName) { result ->
                searchResult.postValue(result)
            }
            BaseActivity.loadingDown()
        }
        return searchResult
    }
}