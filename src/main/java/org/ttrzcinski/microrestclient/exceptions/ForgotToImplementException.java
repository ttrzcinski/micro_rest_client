package org.ttrzcinski.microrestclient.exceptions;

public class ForgotToImplementException extends Exception {

    public ForgotToImplementException(String className) {
        super("Forgot to implement class: " + className);
    }

}
