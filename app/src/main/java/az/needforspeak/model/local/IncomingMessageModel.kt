package az.needforspeak.model.local

import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.packet.Message
import org.jxmpp.jid.EntityBareJid

class IncomingMessageModel (
    val from: EntityBareJid?,
    val message: Message?,
    val chat: Chat?
)