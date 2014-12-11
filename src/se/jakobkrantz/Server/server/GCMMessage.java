package se.jakobkrantz.Server.server;/*
 * Created by krantz on 14-12-11.
 */

import java.util.Map;

public class GCMMessage {

    private final String from;
    private final String messageId;
    private final Map<String, String> payload;

    public GCMMessage(String from, String messageId, Map<String,String> payload){

        this.from = from;
        this.messageId = messageId;
        this.payload = payload;
    }

    public String getFrom() {
        return from;
    }

    public String getMessageId() {
        return messageId;
    }

    public Map<String, String> getPayload() {
        return payload;
    }
}
