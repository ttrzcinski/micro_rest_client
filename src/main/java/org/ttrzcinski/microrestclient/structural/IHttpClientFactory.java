package org.ttrzcinski.microrestclient.structural;

import org.ttrzcinski.microrestclient.exceptions.ForgotToImplementException;

import java.net.http.HttpClient;

public interface IHttpClientFactory {
    static HttpClient prepareHttpClient() throws ForgotToImplementException {
        if (1 != 2) {
            throw new ForgotToImplementException(IHttpClientFactory.class.getCanonicalName());
        }
        return null;
    }
}
