package az.needforspeak.repository

import az.needforspeak.base.BaseApiResponse
import az.needforspeak.data.AuthService
import az.needforspeak.model.remote.auth.response.LoginResponseModel
import az.needforspeak.model.remote.auth.response.ProfileResponseModel
import az.needforspeak.model.remote.auth.request.RegistrationRequestModel
import az.needforspeak.model.remote.auth.response.RegStatusModel
import az.needforspeak.model.remote.auth.response.RegisterResponseModel
import az.needforspeak.utils.Constants
import az.needforspeak.utils.NetworkResult
import az.needforspeak.utils.XMPPController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull


class AuthRepositry(private val service: AuthService): BaseApiResponse() {
    suspend fun login(): Flow<NetworkResult<LoginResponseModel>> {
        return flow {
            emit(safeApiCall { service.login() })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun login(userId: String, password: String): Boolean? {
        return withTimeoutOrNull(Constants.MAIN_TIMEOUT) {
            XMPPController.connect(userId, password)
        }
    }

    suspend fun reConnect(): Boolean? {
        return withTimeoutOrNull(Constants.MAIN_TIMEOUT) {
            XMPPController.reconnect()
        }
    }

    suspend fun getProfile(userId: String): Flow<NetworkResult<ProfileResponseModel>> {
        return flow {
            emit(safeApiCall { service.getProfile(userId) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun sendRegisterRequest(registerRequest: RegistrationRequestModel): Flow<NetworkResult<RegisterResponseModel>> {
        return flow {
            emit(safeApiCall { service.sendRegisterRequest(registerRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun checkRegistrationStatus(requestId: String): Flow<NetworkResult<RegStatusModel>> {
        return flow {
            emit(safeApiCall { service.checkRegistrationStatus(requestId) })
        }.flowOn(Dispatchers.IO)
    }


}