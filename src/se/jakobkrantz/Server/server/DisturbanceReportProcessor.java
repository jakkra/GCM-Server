package se.jakobkrantz.Server.server;

import org.jivesoftware.smack.SmackException;
import org.xml.sax.XMLReader;
import se.jakobkrantz.Server.GcmConstants;
import se.jakobkrantz.Server.database.Database;
import se.jakobkrantz.skanetrafiken.XMLQueryJourneyHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Created by krantz on 14-12-18.
 */
public class DisturbanceReportProcessor implements PayloadProcessor {
    //private XMLReader xmlR;
    //private XMLQueryJourneyHandler xmlQueryJourneyHandler;
    private Map<String, String> payload;

    @Override
    public void handleMessage(GCMMessage message) {
        payload = message.getPayload();
        Database db = null;
        try {
            Database.prepareDatabase();
            db = Database.getInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //Might not be thread safe, monitor needed
        if (db == null) {
            System.err.println("database null");
            return;
        }
        List<String> allRegs = db.getAllRegistrated();
        allRegs.remove(message.getFrom());
        GCMServer server = GCMServer.getInstance();

        for (String regId : allRegs) {
            String jsonMess = JsonMessages.createJsonMessage(regId, GCMServer.nextMessageId(), payload, null, GcmConstants.GCM_DEFAULT_TTL, true);

            try {
                server.sendDownstreamMessage(jsonMess);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }

        }

        //Ack the client, so it knows if the server is online or not.
        HashMap<String,String> ackPayload = new HashMap<String, String>();
        ackPayload.put(GcmConstants.ACTION, GcmConstants.ACTION_ACK);
        String jsonMess = JsonMessages.createJsonMessage(message.getFrom(), GCMServer.nextMessageId(), ackPayload, null, GcmConstants.GCM_DEFAULT_TTL, true);
        try {
            server.sendDownstreamMessage(jsonMess);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }


        //TODO Handle the report and report it to the devices which are interested
//        SAXParserFactory saxPF = SAXParserFactory.newInstance();
//        SAXParser saxP;
//
//        try {
//            saxP = saxPF.newSAXParser();
//            xmlR = saxP.getXMLReader();
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        }
//        xmlQueryJourneyHandler = new XMLQueryJourneyHandler();
//        xmlR.setContentHandler(xmlQueryJourneyHandler);
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    String link = Constants.getURL(Integer.parseInt(payload.get(GcmConstants.DISTURBANCE_FROM_STATION_NBR)), Integer.parseInt(payload.get(GcmConstants.DISTURBANCE_TO_STATION_NBR)), Constants.getCurrentDate(), Constants.getCurrentTime(), 5);
//                    System.out.println(link);
//                    URL url = new URL(link);
//                    xmlR.parse(new InputSource(url.openStream()));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (SAXException e) {
//                    e.printStackTrace();
//                }
//                Database db = null;
//                try {
//                    Database.prepareDatabase();
//                    db = Database.getInstance();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//                //Might not be thread safe, monitor needed
//                if (db == null) {
//                    System.err.println("database null");
//                    return;
//                }
//                ArrayList<Journey> journeys = xmlQueryJourneyHandler.getJourneys();
//
//                //When disturbance is reported it notifies all clients (BAD)
//
//            }
//
//
//        }
//
//        );
//        t.start();
        //TODO handle report from user/client
    }
}
