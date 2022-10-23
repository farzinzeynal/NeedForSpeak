package az.needforspeak.ui.unregister

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.needforspeak.base.BaseActivity
import az.needforspeak.model.remote.auth.LoginResponseModel
import az.needforspeak.model.remote.auth.ProfileResponseModel
import az.needforspeak.repository.AuthRepositry
import az.needforspeak.utils.NetworkResult
import az.needforspeak.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepositry: AuthRepositry): ViewModel() {
    private val _profileResponse: MutableLiveData<NetworkResult<ProfileResponseModel>> = MutableLiveData()
    val profileResponse: LiveData<NetworkResult<ProfileResponseModel>> = _profileResponse
    val showError = MutableLiveData<Boolean>()
    val successLogin = MutableLiveData<Boolean>()
    fun profile(userId: String) = viewModelScope.launch {
        authRepositry.getProfile(userId).collect { values ->
            _profileResponse.value = values
        }
    }

    fun login(context: Context, userName: String, password: String) = run {
        BaseActivity.loadingUp()
        GlobalScope.launch(Dispatchers.IO) {
            try { val result = authRepositry.login(userName, password)
                if (result == true) {
                    SessionManager.saveLogin(context, userName, password)
                    successLogin.postValue(true)
                } else {
                    showError.postValue(true)
                }
                BaseActivity.loadingDown()
            } catch (e: Exception) {
                showError.postValue(true)
                BaseActivity.loadingDown()
            }
        }
    }
}