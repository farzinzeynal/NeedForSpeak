package az.needforspeak.repository

import az.needforspeak.base.BaseApiResponse
import az.needforspeak.data.MainService
import az.needforspeak.model.remote.auth.LoginResponseModel
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

class MainRepository(private val service: MainService) : BaseApiResponse() {

    fun searchUser(userName: String, calback: (ArrayList<MyAccount>?) -> Unit) {
        XMPPController.checkIfUserExists(userName) {
            calback.invoke(it)
        }
    }

    fun sendFriendRequest(userJid: String?) {
        GlobalScope.launch {
            val jid = JidCreate.bareFrom(userJid)
            XMPPController.respondFriendRequest(jid, Presence.Type.subscribe)
        }
    }
}