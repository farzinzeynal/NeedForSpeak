package az.needforspeak.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.needforspeak.base.BaseActivity
import az.needforspeak.model.remote.auth.ProfileResponseModel
import az.needforspeak.model.remote.auth.User
import az.needforspeak.repository.AccountRepository
import az.needforspeak.repository.AuthRepositry
import az.needforspeak.utils.NetworkResult
import az.needforspeak.utils.SessionManager
import az.needforspeak.utils.XMPPController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AccountViewModel(private val accountRepository: AccountRepository): ViewModel() {

    fun getUserDataByUserId(userId: String): MutableLiveData<User> {
        return MutableLiveData<User>().apply {
            viewModelScope.launch {
                if (XMPPController.isConnected())
                    accountRepository.getUserDataById(userId) {
                        postValue(it)
                    }
            }
        }
    }


    fun getUserProfileData(userId: String){


    }

}