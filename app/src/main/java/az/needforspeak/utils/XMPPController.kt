package az.needforspeak.utils

import az.needforspeak.model.remote.auth.AdditionalInfo
import az.needforspeak.model.remote.auth.User
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.StanzaListener
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.provider.ProviderManager
import org.jivesoftware.smack.roster.*
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener
import org.jivesoftware.smackx.search.ReportedData
import org.jivesoftware.smackx.search.UserSearch
import org.jivesoftware.smackx.search.UserSearchManager
import org.jivesoftware.smackx.vcardtemp.VCardManager
import org.jivesoftware.smackx.vcardtemp.packet.VCard
import org.jivesoftware.smackx.xdata.Form
import org.jxmpp.jid.DomainBareJid
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.Jid
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.stringprep.XmppStringprepException
import kotlin.coroutines.resume

object XMPPController {
    private var mConnection: AbstractXMPPConnection? = null
    private val serviceName: DomainBareJid =
        JidCreate.domainBareFrom(Constants.XMPP_JID_SERVICENAME)
    private var userVCard: VCard? = null
    private var isAppForeground = false

    fun connect(userName: String, password: String): Boolean {
        return (
                if (mConnection?.isAuthenticated != true) {
                    try {
                        val config = getConfig(userName, password)
                        mConnection = XMPPTCPConnection(config)
                        mConnection?.replyTimeout = Constants.XMPP_REPLY_TIMEOUT
                        mConnection?.connect()
                        if (mConnection?.isConnected == true) {
                            mConnection?.login()
                            if (mConnection?.isAuthenticated == true) {
                                sendStatus(UserStatusType.AVAILABLE)
                            }
                            SessionManager.initSession(userName)
                            mConnection?.isAuthenticated == true
                        } else
                            false
                    } catch (e: java.lang.Exception) {
                        false
                    }
                } else
                    true
                )
    }

    fun isConnected(): Boolean {
        return mConnection?.isConnected == true
    }

    fun disconnect() {
        if (isConnected()) {
            mConnection?.disconnect(Presence(Presence.Type.unavailable))
            mConnection = null
        }
    }

    private fun getConfig(userName: String, password: String): XMPPTCPConnectionConfiguration? {
        return XMPPTCPConnectionConfiguration.builder()
            .setHost(Constants.XMPP_HOST)
            .setUsernameAndPassword(userName, password)
            .setConnectTimeout(Constants.XMPP_CONNECT_TIMEOUT)
            .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
            .setXmppDomain(serviceName)
            .setSendPresence(true)
            .setDebuggerEnabled(true)
            .setSendPresence(true)
            .build()
    }

    fun addPresenceEventListener(presenceEventListener: PresenceEventListener) {
        runXMPPState {
            mConnection?.let {
                val roster = Roster.getInstanceFor(it)
                roster.addPresenceEventListener(presenceEventListener)
            }
        }

    }

    fun addRosterChangeListener(abstractRosterListener: AbstractRosterListener) {
        runXMPPState {
            mConnection?.let {
                val roster = Roster.getInstanceFor(it)
                roster.addRosterListener(abstractRosterListener)
            }
        }
    }

    fun removeRosterChangeListener(abstractRosterListener: AbstractRosterListener) {
        runXMPPState {
            mConnection?.let {
                val roster = Roster.getInstanceFor(it)
                roster.removeRosterListener(abstractRosterListener)
            }
        }
    }

    fun addRosterLoadedListener(rosterLoadedListener: RosterLoadedListener) {
        runXMPPState {
            mConnection?.let {
                val roster = Roster.getInstanceFor(it)
                roster.addRosterLoadedListener(rosterLoadedListener)
            }
        }
    }

    fun addSubscribeListener(subscribeListener: SubscribeListener) {
        runXMPPState {
            val roster = Roster.getInstanceFor(mConnection)
            roster.addSubscribeListener(subscribeListener)
        }
    }

    fun addUnsubscribeListener() {
        mConnection?.let {
            val listener = UnsubscribeHandler()
            it.addSyncStanzaListener(StanzaListener { packet ->
                val roster = Roster.getInstanceFor(it)
                try {
                    val entry = roster.getEntry(JidCreate.bareFrom(packet.from))
                    roster.removeEntry(entry)
                } catch (e: SmackException.NotConnectedException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } catch (e: SmackException.NoResponseException) {
                    e.printStackTrace()
                } catch (e: XMPPException.XMPPErrorException) {
                    e.printStackTrace()
                } catch (e: XmppStringprepException) {
                    e.printStackTrace()
                }
            }, listener)
        }
    }

    fun getConnection(): AbstractXMPPConnection? {
        return mConnection
    }

    suspend fun getConnectionAsync(): AbstractXMPPConnection? {
        if (mConnection == null || !isConnected())
            reconnect()
        return mConnection
    }

    fun runXMPPState(callback: () -> Unit) {
        if (mConnection == null || !isConnected()) {
            GlobalScope.launch(Dispatchers.IO) {
                reconnect()
                callback.invoke()
            }
        } else
            callback.invoke()
    }

    suspend fun reconnect(): Boolean {
        return suspendCancellableCoroutine { continuation ->
            SessionManager.getCurrentUserJID()
            val userData = SessionManager.getRegisteredUser()
            userData
            continuation.resume(connect(userData?.first ?: "", userData?.second ?: ""))
        }
    }

    fun respondFriendRequest(from: Jid?, responseType: Presence.Type) {
        runXMPPState {
            val subscribed = Presence(responseType)
            subscribed.to = from
            try {
                mConnection?.sendStanza(subscribed)
            } catch (e: SmackException.NotConnectedException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun getRoster(): Roster {
        return Roster.getInstanceFor(mConnection)
    }

    fun sendMessage(message: Message, entityBareJid: EntityBareJid) {
        runXMPPState {
            val chat = ChatManager.getInstanceFor(mConnection).chatWith(entityBareJid)
            chat.send(message)
        }
    }

    fun addOnReceiptReceived(receiptReceivedListener: ReceiptReceivedListener) {
        runXMPPState {
            DeliveryReceiptManager.getInstanceFor(mConnection).apply {
                autoAddDeliveryReceiptRequests()
                addReceiptReceivedListener(receiptReceivedListener)
            }
        }
    }

    fun removeOnReceiptReceived(receiptReceivedListener: ReceiptReceivedListener) {
        DeliveryReceiptManager.getInstanceFor(mConnection)
            .removeReceiptReceivedListener(receiptReceivedListener)
    }

    fun addIncomingMessagesListener(incomingChatMessageListener: IncomingChatMessageListener) {
        runXMPPState {
            val chatManager = ChatManager.getInstanceFor(mConnection)
            chatManager.addIncomingListener(incomingChatMessageListener)
        }
    }

    fun removeIncomingMessagesListener(incomingChatMessageListener: IncomingChatMessageListener) {
        runXMPPState {
            val chatManager = ChatManager.getInstanceFor(mConnection)
            chatManager.addIncomingListener(incomingChatMessageListener)
        }
    }

    fun logout() {
        mConnection?.disconnect()
    }

    fun checkIfUserExists(jid: String?, returnValue: (ArrayList<MyAccount>?) -> Unit) {
        runXMPPState {
            mConnection?.let {
                ProviderManager.addIQProvider("query", "jabber:iq:search", UserSearch.Provider())
                ProviderManager.addIQProvider("query", "jabber:iq:vjud", UserSearch.Provider())
                val searchManager = UserSearchManager(it)
                val searchingUser = try {
                    val services = searchManager.searchServices
                    if (services == null || services.size < 1) returnValue.invoke(null)
                    val searchForm: Form = searchManager.getSearchForm(services[0])
                    val answerForm = searchForm.createAnswerForm()
                    answerForm.setAnswer("user", jid)
                    val data: ReportedData = searchManager.getSearchResults(answerForm, services[0])
                    if (data.rows != null && data.rows.size > 0) {
                        arrayListOf<MyAccount>().also { userList ->
                            data.rows?.forEach { reportedData ->
                                val firstName = reportedData.getValues("first").getIfExists(0)
                                val lastName = reportedData.getValues("last").getIfExists(0)
                                val userJid = reportedData.getValues("jid").getIfExists(0)
                                userList.add(MyAccount(certNumber = userJid, firstname = firstName, lastname = lastName))
                            }
                        }
                    } else null
                } catch (e: Exception) {
                    null
                }
                returnValue.invoke(searchingUser)
            }
        }

    }

    fun getVcardInstance(callback: (VCard) -> Unit) {
        runXMPPState {
            if (userVCard == null && mConnection?.isConnected == true) {
                val vCardManager: VCardManager = VCardManager.getInstanceFor(mConnection)
                userVCard = vCardManager.loadVCard()
            }
            callback.invoke(userVCard!!)
        }
    }


    fun setUserVcard(vCard: VCard) {
        runXMPPState {
            val vCardManager: VCardManager = VCardManager.getInstanceFor(mConnection)
            vCardManager.saveVCard(vCard)
        }
    }

    fun getUserFromUserId(userId: String?, callback: (User) -> Unit) {
        runXMPPState {
            val vCardManager: VCardManager = VCardManager.getInstanceFor(mConnection)
            val userVCard = vCardManager.loadVCard(JidCreate.entityBareFrom(userId))
            callback.invoke(parseVCardData(userVCard))
        }
    }

    fun parseVCardData(userVCard: VCard): User {
        val additionalFields = try {
            val string = userVCard.getField("DESC")
            Gson().fromJson(string, AdditionalInfo::class.java)
        } catch (e: java.lang.Exception) {
            AdditionalInfo()
        } ?: run {
            AdditionalInfo()
        }
        return User("").apply {
            try {
                this.userId = userVCard.from.fromJidToString() ?: ""
                this.userJID = userVCard.from.asUnescapedString()
            } catch (e: java.lang.Exception) {
                ""
            }
            lastName = userVCard.lastName
            firstName = userVCard.firstName
            additional = additionalFields
        }
    }

    fun setAppStatus(isAppForeground: Boolean) {
        this.isAppForeground = isAppForeground
        checkAppStatus()
    }

    private fun checkAppStatus() {
        if (isAppForeground) {
            sendStatus(UserStatusType.AVAILABLE)
        } else {
            sendStatus(UserStatusType.UNAVAILABLE)
        }
    }

    /**
     * Send User statuses as a Presence if app is authenticated
     */
    fun sendStatus(@UserStatusType.UserStatusTypeDef status: Int) {
        if (mConnection?.isAuthenticated == true) {
            val precedence = when (status) {
                UserStatusType.AVAILABLE -> Presence(Presence.Type.available)
                UserStatusType.UNAVAILABLE -> Presence(Presence.Type.unavailable)
                else -> null
            }
            mConnection?.sendStanza(precedence)
        }
    }

}