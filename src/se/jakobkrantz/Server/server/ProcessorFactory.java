package se.jakobkrantz.Server.server;
/*
 * Created by krantz on 14-12-11.
 */


import se.jakobkrantz.Server.GcmConstants;

public class ProcessorFactory {
    public static PayloadProcessor getProcessor(String action) {
        if (action == null) {
            throw new IllegalStateException("action must not be null");
        }
        if (action.equals(GcmConstants.ACTION_REGISTER)) {
            return new RegisterProcessor();
        } else if (action.equals(GcmConstants.ACTION_UNREGISTER)) {
            return new UnRegisterProcessor();
        } else if (action.equals(GcmConstants.ACTION_SET_INTERESTING_LOCATIONS)) {
            return new AddInterestingProcessor();
        } else if (action.equals(GcmConstants.ACTION_REPORT_DISTURBANCE)) {
            return new DisturbanceReportProcessor();
        } else if (action.equals(GcmConstants.ACTION_REMOVE_INTERESTING_LOCATION)) {
            return new RemoveInterestingProcessor();
        }

        throw new IllegalStateException("Action " + action + " is unknown");
    }
}
