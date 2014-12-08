package se.jakobkrantz.Server;/*
 * Created by krantz on 14-12-08.
 */

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.xmlpull.v1.XmlPullParser;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sample Smack implementation of a client for GCM Cloud Connection Server. This
 * code can be run as a standalone CCS client.
 * <p/>
 * <p>For illustration purposes only.
 */
public class GCMServer {

    private static final Logger logger = Logger.getLogger("SmackCcsClient");

    private static final String GCM_SERVER = "gcm.googleapis.com";
    private static final int GCM_PORT = 5236; //TODO Should change to 5235 when testing done

    private static GCMServer sInstance;
    private String projectId;
    private String apiKey;
    private boolean debuggable;

    private XMPPConnection connection;

    /**
     * Indicates whether the connection is in draining state, which means that it
     * will not accept any new downstream messages.
     */
    protected volatile boolean connectionDraining = false;

    public boolean sendDownstreamMessage(String jsonRequest) throws
            NotConnectedException {
        if (!connectionDraining) {
            send(jsonRequest);
            return true;
        }
        logger.info("Dropping downstream message since the connection is draining");
        return false;
    }

    public String nextMessageId() {
        return "m-" + UUID.randomUUID().toString();
    }


    protected void send(String jsonRequest) throws NotConnectedException {
        Packet request = new GcmPacketExtension(jsonRequest).toPacket();
        connection.sendPacket(request);
    }




    /**
     * Connects to GCM Cloud Connection Server using the supplied credentials.
     */
    public void connect() throws XMPPException, IOException, SmackException {
        ConnectionConfiguration config = new ConnectionConfiguration(GCM_SERVER, GCM_PORT);
        config.setSecurityMode(SecurityMode.enabled);
        config.setReconnectionAllowed(true);
        config.setRosterLoadedAtLogin(false);
        config.setSendPresence(false);
        config.setSocketFactory(SSLSocketFactory.getDefault());

        connection = new XMPPTCPConnection(config);
        connection.connect();

        connection.addConnectionListener(new LoggingConnectionListener());

        // Handle incoming packets
        connection.addPacketListener(new PacketProcessor(this,logger), new PacketTypeFilter(Message.class));

        // Log all outgoing packets
        connection.addPacketInterceptor(new PacketInterceptor() {
            @Override
            public void interceptPacket(Packet packet) {
                logger.log(Level.INFO, "Sent: {0}", packet.toXML());
            }
        }, new PacketTypeFilter(Message.class));

        connection.login(projectId + "@gcm.googleapis.com", apiKey);
    }

    public static GCMServer getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("You have to prepare the client first");
        }
        return sInstance;
    }

    public static GCMServer prepareClient(String projectId, String apiKey, boolean debuggable) {
        synchronized (GCMServer.class) {
            if (sInstance == null) {
                sInstance = new GCMServer(projectId, apiKey, debuggable);
            }
        }
        return sInstance;
    }

    private GCMServer(String projectId, String apiKey, boolean debuggable) {
        this();
        this.apiKey = apiKey;
        this.projectId = projectId;
        this.debuggable = debuggable;
    }

    private GCMServer() {
        // Add GcmPacketExtension
        ProviderManager.addExtensionProvider(Constants.GCM_ELEMENT_NAME, Constants.GCM_NAMESPACE, new PacketExtensionProvider() {
            @Override
            public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
                String json = parser.nextText();
                return new GcmPacketExtension(json);
            }
        });
    }

    public synchronized void setConnectionDraining(boolean b) {
        this.connectionDraining = b;
    }

    private static final class LoggingConnectionListener
            implements ConnectionListener {

        @Override
        public void connected(XMPPConnection xmppConnection) {
            logger.info("Connected.");
        }

        @Override
        public void authenticated(XMPPConnection xmppConnection) {
            logger.info("Authenticated.");
        }

        @Override
        public void reconnectionSuccessful() {
            logger.info("Reconnecting..");
        }

        @Override
        public void reconnectionFailed(Exception e) {
            logger.log(Level.INFO, "Reconnection failed.. ", e);
        }

        @Override
        public void reconnectingIn(int seconds) {
            logger.log(Level.INFO, "Reconnecting in %d secs", seconds);
        }

        @Override
        public void connectionClosedOnError(Exception e) {
            logger.info("Connection closed on error.");
        }

        @Override
        public void connectionClosed() {
            logger.info("Connection closed.");
        }
    }
}