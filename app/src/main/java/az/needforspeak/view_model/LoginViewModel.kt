package az.needforspeak.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.needforspeak.base.BaseActivity
import az.needforspeak.model.remote.auth.request.RegisteNewUserModel
import az.needforspeak.model.remote.auth.response.ProfileResponseModel
import az.needforspeak.model.remote.auth.request.RegistrationRequestModel
import az.needforspeak.model.remote.auth.response.RegStatusModel
import az.needforspeak.model.remote.auth.response.RegisterResponseModel
import az.needforspeak.repository.AuthRepositry
import az.needforspeak.utils.NetworkResult
import az.needforspeak.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepositry: AuthRepositry): ViewModel() {

    private val _profileResponse: MutableLiveData<NetworkResult<ProfileResponseModel>> = MutableLiveData()
    val profileResponse: LiveData<NetworkResult<ProfileResponseModel>> = _profileResponse

    private val _registerResponse: MutableLiveData<NetworkResult<RegisterResponseModel>> = MutableLiveData()
    val registerResponse: LiveData<NetworkResult<RegisterResponseModel>> = _registerResponse

    private val _regStatusResponse: MutableLiveData<NetworkResult<RegStatusModel>> = MutableLiveData()
    val regStatusResponse: LiveData<NetworkResult<RegStatusModel>> = _regStatusResponse

    private val _verifyOtpResponse: MutableLiveData<NetworkResult<RegisterResponseModel>> = MutableLiveData()
    val verifyOtpResponse: LiveData<NetworkResult<RegisterResponseModel>> = _verifyOtpResponse

    private val _registerNewUserResponse: MutableLiveData<NetworkResult<String>> = MutableLiveData()
    val registerNewUserResponse: LiveData<NetworkResult<String>> = _registerNewUserResponse

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

    fun sendRegisterRequest(registerRequest: RegistrationRequestModel) = viewModelScope.launch {
        authRepositry.sendRegisterRequest(registerRequest).collect { values ->
            _registerResponse.value = values
            BaseActivity.loadingDown()
        }
    }

    fun checkRegistrationStatus(requestId: Int)= viewModelScope.launch  {
        BaseActivity.loadingUp()
        authRepositry.checkRegistrationStatus(requestId).collect { values ->
            _regStatusResponse.value = values
            BaseActivity.loadingDown()
        }
    }

    fun sendOtpRequest(regId: Int) = viewModelScope.launch {
        BaseActivity.loadingUp()
        authRepositry.sendOtp(regId).collect { values ->
            BaseActivity.loadingDown()
        }
    }

    fun verifyCode(regId: Int, otpCode: String) = viewModelScope.launch {
        BaseActivity.loadingUp()
        authRepositry.verifyOtp(regId,otpCode).collect { values ->
            _verifyOtpResponse.value = values
            BaseActivity.loadingDown()
        }
    }

    fun registerNewUser(regId: Int,name: String, sureName: String, password: String) = viewModelScope.launch {
        BaseActivity.loadingUp()
        authRepositry.registerNewUser(RegisteNewUserModel(regId,name,sureName,password)).collect { values ->
            _registerNewUserResponse.value = values
            BaseActivity.loadingDown()
        }
    }
}