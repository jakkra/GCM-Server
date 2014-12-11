package se.jakobkrantz.Server.server;/*
 * Created by krantz on 14-12-11.
 */

import java.util.Map;

public interface PayloadProcessor {

    public void handleMessage(Map<String, Object> jsonObject);
}
