package se.jakobkrantz.Server.server;
/*
 * Created by krantz on 14-12-11.
 */

public class ProcessorFactory {


    public static String ACTION_REGISTER = "regUserIdAction";
    public static String ACTION_UNREGISTER = "unRegUserIdAction";
    public static String ACTION_SET_INTERESTING_LOCATIONS = "actionLocationsInteresting";

    public static PayloadProcessor getProcessor(String action) {
        if (action == null) {
            throw new IllegalStateException("action must not be null");
        }
        if (action.equals(ACTION_REGISTER)) {
            return new RegisterProcessor();
        } else if (action.equals(ACTION_UNREGISTER)) {
            return new UnRegisterProcessor();
        } else if (action.equals(ACTION_SET_INTERESTING_LOCATIONS)) {
            return new AddInterestingProcessor();
        }
        throw new IllegalStateException("Action " + action + " is unknown");
    }
}
