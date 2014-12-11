package se.jakobkrantz.Server;/*
 * Created by krantz on 14-12-08.
 */

public  class Constants {

    public static final String GCM_ELEMENT_NAME = "gcm";
    public static final String GCM_NAMESPACE = "google:mobile:data";

    public static String ACTION = "action";

    public static String ACTION_REGISTER = "regUserIdAction";
    public static String ACTION_UNREGISTER = "unRegUserIdAction";
    public static String ACTION_SET_INTERESTING_LOCATIONS = "actionLocationsInteresting";

    public static long GCM_DEFAULT_TTL = 60 * 60 * 1000; // 1 hour
}
