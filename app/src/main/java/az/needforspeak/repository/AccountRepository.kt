package az.needforspeak.repository

import az.needforspeak.base.BaseApiResponse
import az.needforspeak.data.AccountService
import az.needforspeak.data.MainService
import az.needforspeak.model.remote.auth.InputValueModel
import az.needforspeak.model.remote.auth.LoginResponseModel
import az.needforspeak.model.remote.auth.ProfileResponseModel
import az.needforspeak.model.remote.auth.User
import az.needforspeak.utils.MyAccount
import az.needforspeak.utils.NetworkResult
import az.needforspeak.utils.XMPPController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.jivesoftware.smack.packet.Presence
import org.jxmpp.jid.impl.JidCreate

class AccountRepository(private val service: AccountService) : BaseApiResponse() {


    fun getUserDataById(userId: String, callback: (User?) -> Unit)  {
        GlobalScope.launch(Dispatchers.IO) {
            //val localDataStore = usersDao?.getUserById(userId.fromJidToString())
            //callback(localDataStore)
            XMPPController.getUserFromUserId(userId) { remoteDataStore ->
                callback(remoteDataStore)
                remoteDataStore
            }
        }
    }

    suspend fun getProfile(userId: String): Flow<NetworkResult<ProfileResponseModel>> {
        return flow {
            emit(safeApiCall { service.getProfile(userId) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateProfileData(userId: String, key:String, value: String, isVisible: Boolean): Flow<NetworkResult<String>> {
        return flow {
            emit(safeApiCall { service.updateProfileData(userId,key,InputValueModel(value,isVisible)) })
        }.flowOn(Dispatchers.IO)
    }



}