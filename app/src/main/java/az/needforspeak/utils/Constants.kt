package az.needforspeak.utils

import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.impl.JidCreate

object Constants {
    const val NFS_PARENT_PATH = "NFS"
    const val VOICES_PARENT_PATH = "VOICES"
    const val IMAGES_PARENT_PATH = "IMAGES"
    const val PROFILES = "PROFILES"
    const val POSTS_PARENT_PATH = "POSTS"
    const val PROFILE_IMAGES = "images"
    const val LOCAL_PROFILE_IMAGES = "profiles"
    const val DOCUMENTS_PARENT_PATH = "documents"
    const val XMPP_JID_SERVICENAME = "im.needforspeak.xyz"
    const val XMPP_HOST = "ejabberd.needforspeak.xyz"
    const val XMPP_CONCEREANCE_ROOM = "conference.im.needforspeak.xyz"
    const val XMPP_CONNECT_TIMEOUT = 15000
    const val XMPP_REPLY_TIMEOUT: Long = 15000
    const val MAIN_TIMEOUT = 15000L
    var showingChatId: String? = null
    const val username = "10-AA-009"

    //    const val username = "con"
    const val password = "namiq123"

    const val BASE_URL = "https://api.needforspeak.xyz"
    const val DOCUMENT_URL = "$BASE_URL/document/"
    const val CONNECT_TIMEOUT: Long = 45
    const val READ_TIMEOUT: Long = 45

    fun getCurrentUser(): String {
        return "$username@$XMPP_HOST"
    }

    fun getCurrentUserString(): String {
        return username
    }

    fun getCurrentUserAsEntityBareJid(): EntityBareJid {
        return JidCreate.entityBareFrom("$username@$XMPP_HOST")
    }

    fun isCurrentUser(userId: String): Boolean {
        return "$username@$XMPP_HOST" == userId
    }

    const val REQUEST_ID: String = "request_id"
    const val APPROVAL_RESULT: String = "approval_result"

}

object SharedConstants {
    const val LANGUAGE = "lang"
    const val NOTIFICATION_SOUND = "app_sound"
    const val NOTIFICATION_TOKEN = "notification_token"
    const val PENDING_TOKEN = "notification_pending"
    const val ISUSERDATALOADED = "isUSerDataLoaded"
}

object XMPPExtentions {
    const val MAIN_DATA_EXTENSION = "urn:xmpp:data"
    const val TIMESTAMP = "timestamp"
    const val DATA = "data"
    const val ISFORWARDED = "isForwarded"
    const val TYPE = "type"
    const val FILE_EXTENSION = "message:file"
    const val FILE_EXTENSION_POST = "urn:xmpp:file"
    const val FILES_EXTENSION_POST = "urn:xmpp:files"
    const val FILE = "file"
    const val FILES = "files"
    const val URL = "url"

    const val GROUPEVENT = "event"
    const val GROUPPAYLOAD = "http://jabber.org/protocol/pubsub#event"

    const val PARENT = "parent"
    const val PARENT_EXTENSION = "urn:xmpp:parent"
    const val PARENT_ID = "parent_id"

    const val REPLY_EXTENSION = "urn:xmpp:forward:0"
    const val FORWARDED = "forwarded"
    const val POST = "urn:xmpp:forward:0"


}