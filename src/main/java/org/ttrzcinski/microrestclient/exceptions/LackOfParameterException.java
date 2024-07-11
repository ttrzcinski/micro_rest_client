package org.ttrzcinski.microrestclient.exceptions;

public class LackOfParameterException extends Exception {
    public LackOfParameterException(String paramName) {
        super("Lack of Parameter: " + paramName);
    }

    public LackOfParameterException(String paramName, Throwable cause) {
        super("Lack of Parameter: " + paramName, cause);
    }

    public LackOfParameterException(Throwable cause) {
        super(cause);
    }

    protected LackOfParameterException(String paramName, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super("Lack of Parameter: " + paramName, cause, enableSuppression, writableStackTrace);
    }
}