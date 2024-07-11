package org.ttrzcinski.microrestclient.structural;

import org.ttrzcinski.microrestclient.exceptions.ForgotToImplementException;

public abstract class IDebuggable {
    private static boolean debugFlag = false;

    protected static boolean isInDebug(){
        return debugFlag;
    }

    protected static void turnDebugOn() throws ForgotToImplementException {
        throw new ForgotToImplementException(IDebuggable.class.getCanonicalName());
    }

    protected static void turnDebugOff() throws ForgotToImplementException {
        throw new ForgotToImplementException(IDebuggable.class.getCanonicalName());
    }

    protected static void debug(String entry) {
        boolean currentStateOfFlag = isInDebug();
        if (currentStateOfFlag) {
            System.out.println("[DEBUG] " + entry);
        }
    }
}
