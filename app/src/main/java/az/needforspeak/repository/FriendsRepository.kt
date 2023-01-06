package az.needforspeak.repository

import az.needforspeak.base.BaseApiResponse
import az.needforspeak.data.FriendsService
import az.needforspeak.model.remote.auth.response.UserSearchResponse
import az.needforspeak.utils.NetworkResult
import az.needforspeak.utils.XMPPController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.jivesoftware.smack.packet.Presence
import org.jxmpp.jid.impl.JidCreate

class FriendsRepository(private val service: FriendsService) : BaseApiResponse() {

    suspend fun searchUser(userId: String): Flow<NetworkResult<UserSearchResponse>> {
        return flow {
            emit(safeApiCall { service.searchUser(userId) })
        }.flowOn(Dispatchers.IO)
    }

    fun sendFriendRequest(userJid: String?) {
        GlobalScope.launch {
            val jid = JidCreate.bareFrom(userJid)
            XMPPController.respondFriendRequest(jid, Presence.Type.subscribe)
        }
    }
}