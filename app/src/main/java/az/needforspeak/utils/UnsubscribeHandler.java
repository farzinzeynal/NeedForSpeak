package az.needforspeak.utils;

import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;

public class UnsubscribeHandler implements StanzaFilter {

    @Override
    public boolean accept(Stanza stanza) {
        if (stanza instanceof Presence) {
            Presence presence = (Presence) stanza;
            return presence.getType() == Presence.Type.unsubscribe;
        }
        return false;
    }
}

