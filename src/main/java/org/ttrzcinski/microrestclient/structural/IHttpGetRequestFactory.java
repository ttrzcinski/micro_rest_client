package org.ttrzcinski.microrestclient.structural;

import org.ttrzcinski.microrestclient.exceptions.ForgotToImplementException;

import java.net.http.HttpClient;

public interface IHttpGetRequestFactory {
    static HttpClient getHttpRequest() throws ForgotToImplementException {
        if (1 != 2) {
            throw new ForgotToImplementException(IHttpGetRequestFactory.class.getCanonicalName());
        }
        return null;
    }
}
