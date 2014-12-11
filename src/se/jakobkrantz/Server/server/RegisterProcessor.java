package se.jakobkrantz.Server.server;/*
 * Created by krantz on 14-12-11.
 */

import org.jivesoftware.smack.SmackException;
import se.jakobkrantz.Server.Constants;
import se.jakobkrantz.Server.database.Database;

import java.util.Map;

public class RegisterProcessor implements PayloadProcessor {
    @Override
    public void handleMessage(Map<String, Object> jsonObject) {
        Database db = Database.getInstance();
        String from = (String) jsonObject.get("from");
        db.register(from);
        String jsonMess = JsonMessages.createJsonMessage(from, GCMServer.nextMessageId(), null, null, Constants.GCM_DEFAULT_TTL, true);
        try {
            GCMServer.getInstance().sendDownstreamMessage(jsonMess);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }
}
