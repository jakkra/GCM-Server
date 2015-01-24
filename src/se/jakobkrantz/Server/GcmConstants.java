package se.jakobkrantz.Server;/*
 * Created by krantz on 14-12-08.
 */

public class GcmConstants {

    public static final String GCM_ELEMENT_NAME = "gcm";
    public static final String GCM_NAMESPACE = "google:mobile:data";
    public static final String ACTION_REMOVE_INTERESTING_LOCATION = "actionRemoveInterestingLocation";

    public static String ACTION = "action";

    public static String ACTION_REGISTER = "regUserIdAction";
    public static String ACTION_UNREGISTER = "unRegUserIdAction";
    public static String ACTION_SET_INTERESTING_LOCATIONS = "actionLocationsInteresting";
    public static String ACTION_REPORT_DISTURBANCE = "reportDisturbance";

    public static String DISTURBANCE_FROM_STATION_NBR = "distFrom";
    public static String DISTURBANCE_TO_STATION_NBR = "distTo";
    public static String DISTURBANCE_APPROX_MINS = "disturbMins";
    public static String DISTURBANCE_NOTE = "distNote";

    public static long GCM_DEFAULT_TTL = 60 * 60 * 1000; // 1 hour
}
