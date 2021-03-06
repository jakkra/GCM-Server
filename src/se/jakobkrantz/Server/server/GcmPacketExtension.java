package se.jakobkrantz.Server.server;/*
 * Created by krantz on 14-12-08.
 */

import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import se.jakobkrantz.Server.GcmConstants;

/**
 * XMPP Packet Extension for GCM Cloud Connection Server.
 */
public final class GcmPacketExtension extends DefaultPacketExtension {

    private final String json;

    public GcmPacketExtension(String json) {
        super(GcmConstants.GCM_ELEMENT_NAME, GcmConstants.GCM_NAMESPACE);
        this.json = json;
    }

    public String getJson() {
        return json;
    }

    @Override
    public String toXML() {
        return String.format("<%s xmlns=\"%s\">%s</%s>",
                GcmConstants.GCM_ELEMENT_NAME, GcmConstants.GCM_NAMESPACE,
                StringUtils.escapeForXML(json), GcmConstants.GCM_ELEMENT_NAME);
    }

    public Packet toPacket() {
        Message message = new Message();
        message.addExtension(this);
        return message;
    }
}