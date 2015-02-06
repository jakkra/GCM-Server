package se.jakobkrantz.Server.server;/*
 * Created by krantz on 14-12-11.
 */

import org.jivesoftware.smack.SmackException;
import se.jakobkrantz.Server.GcmConstants;
import se.jakobkrantz.Server.database.Database;

import java.util.HashMap;

public class UnRegisterProcessor implements PayloadProcessor {
    @Override
    public void handleMessage(GCMMessage message) {
        System.out.println("Unregister request");
        Database db = Database.getInstance();
        String from = message.getFrom();
        System.out.println(db.getAllRegistrated());
        db.unregister(from);
        System.out.println(db.getAllRegistrated());
        HashMap<String,String> payload = new HashMap<String, String>();
        payload.put(GcmConstants.ACTION, GcmConstants.ACTION_UNREGISTER_SUCCESSFUL);
        String jsonMess = JsonMessages.createJsonMessage(from, GCMServer.nextMessageId(), payload, null, GcmConstants.GCM_DEFAULT_TTL, true);
        try {
            GCMServer.getInstance().sendDownstreamMessage(jsonMess);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }
}
