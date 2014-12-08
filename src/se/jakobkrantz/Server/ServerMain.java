package se.jakobkrantz.Server;/*
 * Created by krantz on 14-12-08.
 */

import java.util.HashMap;
import java.util.Map;

public class ServerMain {

    public static void main(String[] args) throws Exception {
        final String projectId = "1234567890"; //GCM sender id
        final String apiKey = "API key"; // google servers not working so can't retrieve one
        JsonMessages jsonMessages = new JsonMessages();
        GCMServer gcmServer = GCMServer.prepareClient(projectId, apiKey, true);
        gcmServer.connect();

        // Send a sample hello downstream message to a device.
//        String toRegId = "RegistrationIdOfTheTargetDevice";
//        String messageId = gcmServer.nextMessageId();
//        Map<String, String> payload = new HashMap<String, String>();
//        payload.put("Hello", "World");
//        payload.put("CCS", "Dummy Message");
//        payload.put("EmbeddedMessageId", messageId);
//        String collapseKey = "sample";
//        Long timeToLive = 10000L;
//        String message = jsonMessages.createJsonMessage(toRegId, messageId, payload, collapseKey, timeToLive, true);
//
//        gcmServer.sendDownstreamMessage(message);
    }
}
