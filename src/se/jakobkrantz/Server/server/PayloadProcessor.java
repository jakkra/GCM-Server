package se.jakobkrantz.Server.server;/*
 * Created by krantz on 14-12-11.
 */


public interface PayloadProcessor {

    public void handleMessage(GCMMessage message);
}
