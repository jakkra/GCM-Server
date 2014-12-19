package se.jakobkrantz.Server.server;
/*
 * Created by krantz on 14-12-11.
 */

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import se.jakobkrantz.Server.GcmConstants;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PacketProcessor implements PacketListener {

    private final GCMServer gcmServer;
    private Logger logger;
    private JsonMessages jsonBuilder;

    public PacketProcessor(GCMServer gcmServer, Logger logger) {
        this.gcmServer = gcmServer;
        this.logger = logger;
        jsonBuilder = new JsonMessages();
    }

    @Override
    public void processPacket(Packet packet) throws SmackException.NotConnectedException {

        Message incomingMessage = (Message) packet;
        GcmPacketExtension gcmPacket = (GcmPacketExtension) incomingMessage.getExtension(GcmConstants.GCM_NAMESPACE);
        String json = gcmPacket.getJson();
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> jsonObject = (Map<String, Object>) JSONValue.parseWithException(json);

            // present for "ack"/"nack", null otherwise
            Object messageType = jsonObject.get("message_type");
            if (messageType == null) {
                // Normal upstream data message
                handleUpstreamMessage(jsonObject);

                // Send ACK gcm
                String messageId = (String) jsonObject.get("message_id");
                String from = (String) jsonObject.get("from");
                String ack = jsonBuilder.createJsonAck(from, messageId);
                gcmServer.send(ack);
            } else if ("ack".equals(messageType.toString())) {
                // Process Ack
                handleAckReceipt(jsonObject);
            } else if ("nack".equals(messageType.toString())) {
                // Process Nack
                handleNackReceipt(jsonObject);
            } else if ("control".equals(messageType.toString())) {
                // Process control message
                handleControlMessage(jsonObject);
            } else {
                logger.log(Level.WARNING, "Unrecognized message type (%s)", messageType.toString());
            }
        } catch (ParseException e) {
            logger.log(Level.SEVERE, "Error parsing JSON " + json, e);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to process packet", e);
        }
    }

    /**
     * Handles an ACK.
     * Logs a INFO message
     * TODO: Implement the right way
     */
    public void handleAckReceipt(Map<String, Object> jsonObject) {
        String messageId = (String) jsonObject.get("message_id");
        String from = (String) jsonObject.get("from");
        logger.log(Level.INFO, "handleAckReceipt() from: " + from + ", messageId: " + messageId);
    }

    /**
     * Handles a NACK.
     * Logs a INFO message
     * TODO: Implement the right way
     */
    public void handleNackReceipt(Map<String, Object> jsonObject) {
        String messageId = (String) jsonObject.get("message_id");
        String from = (String) jsonObject.get("from");
        logger.log(Level.INFO, "handleNackReceipt() from: " + from + ",messageId: " + messageId);
    }

    public void handleControlMessage(Map<String, Object> jsonObject) {
        logger.log(Level.INFO, "handleControlMessage(): " + jsonObject);
        String controlType = (String) jsonObject.get("control_type");
        if ("CONNECTION_DRAINING".equals(controlType)) {
            gcmServer.setConnectionDraining(true);
        } else {
            logger.log(Level.INFO, "Unrecognized control type: %s. This could happen if new features are " + "added to the XMPP protocol.", controlType);
        }
    }

    /**
     * Handles an upstream data message from a device application.
     */
    public void handleUpstreamMessage(Map<String, Object> jsonObject) {
        Map<String, String> payload = (Map<String, String>) jsonObject.get("data");
        GCMMessage gcmMessage = new GCMMessage((String) jsonObject.get("from"), (String) jsonObject.get("message_id"), payload);
        if (payload.get(GcmConstants.ACTION) != null) {
            PayloadProcessor processor = ProcessorFactory.getProcessor(payload.get(GcmConstants.ACTION));
            processor.handleMessage(gcmMessage);
        } else {
            System.out.println(jsonObject.get(GcmConstants.ACTION));
            throw new IllegalStateException("UpstreamMessage must contain an ACTION");
        }

    }
}
