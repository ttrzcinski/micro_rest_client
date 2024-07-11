package org.ttrzcinski.microrestclient.structural;

import org.ttrzcinski.microrestclient.exceptions.ForgotToImplementException;

public interface ISingleton {

    static Object getInstance() throws ForgotToImplementException {
        throw new ForgotToImplementException(ISingleton.class.getCanonicalName());
    }

}
