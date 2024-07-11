package org.ttrzcinski;

import org.ttrzcinski.microrestclient.MicroRestClient;
import org.ttrzcinski.microrestclient.exceptions.LackOfParameterException;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static java.util.Arrays.stream;
import static org.ttrzcinski.microrestclient.MicroRestClient.error;
import static org.ttrzcinski.microrestclient.MicroRestClient.getInstance;


public class Main {

    public static void main(String[] args) {
        // Process given args
        // TODO MOVE ARGS PROCESSING INSIDE MicroRestClient
        boolean useGet = false;
        String givenUri = null;
        if (args != null) {
            useGet = stream(args).anyMatch(elem -> elem.startsWith("GET"));
            Optional<String> tempGivenUri = stream(args).filter(elem -> elem.startsWith("uri=")).findFirst();
            if (tempGivenUri.isPresent() && tempGivenUri.get().contains("=")) {
                // TODO ADD HERE CHECK FOR AT LEAST 3 CHARS IN END-STRING
                givenUri = tempGivenUri.get().split("=")[1];
            }
        }

        // Obtain server's instance
        MicroRestClient client = getInstance();

        // Prepare GET Request
        HttpRequest request;
        try {
            request = givenUri != null ? client.prepareGet(givenUri): client.prepareGet();
        } catch (LackOfParameterException thrown) {
            error("Couldn't prepare HTTP GET Request.", thrown);
            return;
        }

        HttpResponse<String> response = client.sendGetWithSneakyThrows(request);
        if (response == null) {
            error("Couldn't send HTTP GET Request.");
            return;
        }

        // Print response's details
        MicroRestClient.debugHeaders(response);
        MicroRestClient.debugStatusCode(response);
        MicroRestClient.debugBody(response);

        if (response.statusCode() == 200) {
            MicroRestClient.info("It was OK - 200.");
        }

        // Kill the server
        client.kill();

    }

}