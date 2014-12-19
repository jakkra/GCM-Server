package se.jakobkrantz.Server.server;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import se.jakobkrantz.Server.GcmConstants;
import se.jakobkrantz.Server.database.Database;
import se.jakobkrantz.skanetrafiken.Constants;
import se.jakobkrantz.skanetrafiken.Journey;
import se.jakobkrantz.skanetrafiken.XMLQueryJourneyHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

/*
 * Created by krantz on 14-12-18.
 */
public class DisturbanceReportProcessor implements PayloadProcessor {
    private XMLReader xmlR;
    private XMLQueryJourneyHandler xmlQueryJourneyHandler;
    private Map<String, String> payload;

    @Override
    public void handleMessage(GCMMessage message) {
        payload = message.getPayload();
        SAXParserFactory saxPF = SAXParserFactory.newInstance();
        SAXParser saxP;

        try {
            saxP = saxPF.newSAXParser();
            xmlR = saxP.getXMLReader();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        xmlQueryJourneyHandler = new XMLQueryJourneyHandler();
        xmlR.setContentHandler(xmlQueryJourneyHandler);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String link = Constants.getURL(Integer.parseInt(payload.get(GcmConstants.DISTURBANCE_FROM_STATION_NBR)), Integer.parseInt(payload.get(GcmConstants.DISTURBANCE_TO_STATION_NBR)), Constants.getCurrentDate(), Constants.getCurrentTime(), 5);
                    System.out.println(link);
                    URL url = new URL(link);
                    xmlR.parse(new InputSource(url.openStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
                Database db = null;
                try {
                    Database.prepareDatabase();
                    db = Database.getInstance();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                //Might not be thread safe, monitor needed
                if(db == null){
                    System.err.println("database null");
                    return;
                }
                ArrayList<Journey> journeys = xmlQueryJourneyHandler.getJourneys();
                //TODO getJourneys() is null???
                System.out.println(journeys.size());
                System.out.println(journeys.get(0).toString());


            }
        });
        t.start();
        //TODO handle report from user/client
    }
}
