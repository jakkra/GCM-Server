package se.jakobkrantz.Server.server;/*
 * Created by krantz on 14-12-08.
 */

import org.json.simple.JSONValue;

import java.util.HashMap;
import java.util.Map;

public class JsonMessages {

    /**
     * Creates a JSON encoded GCM message.
     *
     * @param to RegistrationId of the target device (Required).
     * @param messageId Unique messageId for which CCS will send an
     *         "ack/nack" (Required).
     * @param payload Message content intended for the application. (Optional).
     * @param collapseKey GCM collapse_key parameter (Optional).
     * @param timeToLive GCM time_to_live parameter (Optional).
     * @param delayWhileIdle GCM delay_while_idle parameter (Optional).
     * @return JSON encoded GCM message.
     */
    public static String createJsonMessage(String to, String messageId,
                                           Map<String, String> payload, String collapseKey, Long timeToLive,
                                           Boolean delayWhileIdle) {
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("to", to);
        if (collapseKey != null) {
            message.put("collapse_key", collapseKey);
        }
        if (timeToLive != null) {
            message.put("time_to_live", timeToLive);
        }
        if (delayWhileIdle != null && delayWhileIdle) {
            message.put("delay_while_idle", true);
        }
        message.put("message_id", messageId);
        message.put("data", payload);
        return JSONValue.toJSONString(message);
    }

    /**
     * Creates a JSON encoded ACK message for an upstream message received
     * from an application.
     *
     * @param to RegistrationId of the device who sent the upstream message.
     * @param messageId messageId of the upstream message to be acknowledged to CCS.
     * @return JSON encoded ack.
     */
    public String createJsonAck(String to, String messageId) {
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("message_type", "ack");
        message.put("to", to);
        message.put("message_id", messageId);
        return JSONValue.toJSONString(message);
    }
}
