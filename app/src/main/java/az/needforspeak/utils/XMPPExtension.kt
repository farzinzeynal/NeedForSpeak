package az.needforspeak.utils

import org.jxmpp.jid.Jid
import org.jxmpp.util.XmppStringUtils
import java.util.*


fun Jid?.fromJidToString(): String? {
    return XmppStringUtils.parseLocalpart(this?.asUnescapedString()).toUpperCase(Locale.getDefault())
}

fun String?.fromJidToString(): String {
    return XmppStringUtils.parseLocalpart(this).toUpperCase(Locale.getDefault())
}

fun String?.fromStringToJid(): String {
    return "$this@${Constants.XMPP_HOST}"
}

fun String?.fromStringToConferenceJid(): String {
    return "$this@${Constants.XMPP_CONCEREANCE_ROOM}"
}

