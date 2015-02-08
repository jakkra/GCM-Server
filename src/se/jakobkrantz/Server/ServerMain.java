package se.jakobkrantz.Server;/*
 * Created by krantz on 14-12-08.
 */

import se.jakobkrantz.Server.database.Database;
import se.jakobkrantz.Server.server.GCMServer;
import se.jakobkrantz.Server.server.JsonMessages;

public class ServerMain {

    public static void main(String[] args) throws Exception {
        final String projectId = "24223089278";
        final String apiKey = "AIzaSyCc16CWdJ9bAGpRto4FeIOceJlDWyV6LQo";
        Database.prepareDatabase();
        GCMServer gcmServer = GCMServer.prepareClient(projectId, apiKey);
        gcmServer.connect(true);

    }
}
