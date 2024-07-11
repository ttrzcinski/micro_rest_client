package org.ttrzcinski.microrestclient.structural;

import org.ttrzcinski.microrestclient.exceptions.ForgotToImplementException;

public interface IKillable {
    default void kill() throws ForgotToImplementException {
        throw new ForgotToImplementException(IKillable.class.getCanonicalName());
    }
}
