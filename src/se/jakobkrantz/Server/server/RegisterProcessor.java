package se.jakobkrantz.Server.server;/*
 * Created by krantz on 14-12-11.
 */

import org.jivesoftware.smack.SmackException;
import se.jakobkrantz.Server.GcmConstants;
import se.jakobkrantz.Server.database.Database;

public class RegisterProcessor implements PayloadProcessor {
    @Override
    public void handleMessage(GCMMessage message) {
        Database db = Database.getInstance();
        String from = message.getPayload().get("from");
        db.register(from);
        String jsonMess = JsonMessages.createJsonMessage(from, GCMServer.nextMessageId(), null, null, GcmConstants.GCM_DEFAULT_TTL, true);
        try {
            GCMServer.getInstance().sendDownstreamMessage(jsonMess);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }
}
