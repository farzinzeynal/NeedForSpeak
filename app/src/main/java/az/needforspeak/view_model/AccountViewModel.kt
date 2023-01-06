package az.needforspeak.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.needforspeak.model.remote.auth.response.ProfileResponseModel
import az.needforspeak.repository.AccountRepository
import az.needforspeak.utils.NetworkResult
import kotlinx.coroutines.launch

class AccountViewModel(private val accountRepository: AccountRepository): ViewModel() {

    private val _profileResponse: MutableLiveData<NetworkResult<ProfileResponseModel>> = MutableLiveData()
    val profileResponse: LiveData<NetworkResult<ProfileResponseModel>> = _profileResponse

    private val _updateProfileResponse: MutableLiveData<NetworkResult<String>> = MutableLiveData()
    val updateProfileResponse: LiveData<NetworkResult<String>> = _updateProfileResponse


    fun getUserProfileData(userId: String) = viewModelScope.launch {
        accountRepository.getProfile(userId).collect { values ->
            _profileResponse.value = values
        }
    }


    fun updateProfileData(userId: String, key:String, value: String, isVisible: Boolean) = viewModelScope.launch {
        accountRepository.updateProfileData(userId,key,value,isVisible).collect { values ->
            _updateProfileResponse.value = values
        }
    }




}