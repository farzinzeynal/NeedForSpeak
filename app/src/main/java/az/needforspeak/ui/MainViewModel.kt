package az.needforspeak.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.needforspeak.model.remote.auth.LoginResponseModel

import az.needforspeak.repository.AuthRepositry
import az.needforspeak.utils.NetworkResult
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val authRepositry: AuthRepositry): ViewModel() {
    private val _loginResponse: MutableLiveData<NetworkResult<LoginResponseModel>> = MutableLiveData()
    val loginResponse: LiveData<NetworkResult<LoginResponseModel>> = _loginResponse
    fun login() = viewModelScope.launch {
        authRepositry.login().collect { values ->
            _loginResponse.value = values
        }
    }
}