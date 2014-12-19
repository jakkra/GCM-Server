package se.jakobkrantz.Server.server;/*
 * Created by krantz on 14-12-11.
 */

public class AddInterestingProcessor implements PayloadProcessor {
    @Override
    public void handleMessage(GCMMessage message) {
        System.out.println(message.getPayload().toString());
        //TODO
    }
}
