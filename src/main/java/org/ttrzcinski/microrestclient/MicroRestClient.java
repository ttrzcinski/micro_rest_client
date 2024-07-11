package org.ttrzcinski.microrestclient;

import org.ttrzcinski.microrestclient.exceptions.ForgotToImplementException;
import org.ttrzcinski.microrestclient.exceptions.LackOfParameterException;
import org.ttrzcinski.microrestclient.structural.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.time.Duration;
import java.util.Arrays;

import static org.ttrzcinski.microrestclient.structural.ILog.d;
import static org.ttrzcinski.microrestclient.structural.ILog.e;

public class MicroRestClient extends IDebuggable implements
        ISingleton, // one instance will be enough
        IHttpClientFactory, // provides HTTP Client
        IHttpGetRequestFactory, // provides HTTP GET Request
        ILog, // uses log
        IKillable // is killable as should be closed after use
{

    /**
     * Sends HTTP GET Request with wanted Response type
     *
     * @param request      given HTTP GET Request
     * @param responseType wanted Response Type
     * @param <T>          pointed Type within HTTP response
     * @return HTTP GET Response with wanted type
     * @throws IOException          can occur, if client is shut down, brake in sending or receiving
     * @throws InterruptedException in case request was badly formed
     * @throws SecurityException    in case, if caller is not entitled to send request
     */
    public <T> HttpResponse<T> sendGet(HttpRequest request, BodyHandler<T> responseType)
            throws IOException, InterruptedException {
        return prepareHttpClient().send(request, responseType);
    }

    /**
     * Sends HTTP GET Request with DEFAULT STRING RESPONSE.
     *
     * @param request given HTTP GET Request
     * @return Http response as String
     * @throws IOException          can occur, if client is shut down, brake in sending or receiving
     * @throws InterruptedException in case request was badly formed
     * @throws SecurityException    in case, if caller is not entitled to send request
     */
    public HttpResponse<String> sendGet(HttpRequest request)
            throws IOException, InterruptedException {
        return prepareHttpClient().send(request, DEFAULT_GET_RESPONSE_TYPE);
    }

    /**
     * Sends HTTP GET Request with DEFAULT STRING RESPONSE and skips error handling.
     *
     * @param request given HTTP GET Request
     * @return Http response as String, if worked, null otherwise
     */
    public HttpResponse<String> sendGetWithSneakyThrows(HttpRequest request) {
        try {
            return sendGet(request);
        } catch (Exception thrown) {
            error("Couldn't send HTTP GET Request.", thrown);
        }
        return null;
    }

    // TODO ADD VERSION WITH PREDEFINED COLLECTION OF HEADERS

    /**
     * Prepares simple Http GET Request with wanted URI.
     *
     * @param uri wanted URI
     * @return prepared HTTP GET Request
     * @throws LackOfParameterException in case uri was not provided
     */
    public HttpRequest prepareGet(String uri) throws LackOfParameterException {
        if (uri == null || uri.trim().isEmpty()) {
            throw new LackOfParameterException("uri");
        }

        return HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri))
                .setHeader("User-Agent", "JDK11 Lightweight HttpClient")
                .build();
    }

    /**
     * Prepares simple Http GET Request with wanted URI and headers.
     *
     * @param uri     wanted URI
     * @param headers given headers
     * @return prepared HTTP GET Request
     * @throws LackOfParameterException in case uri was not provided
     */
    public HttpRequest prepareGet(String uri, String[] headers) throws LackOfParameterException {
        if (uri == null || uri.trim().isEmpty()) {
            throw new LackOfParameterException("uri");
        }

        // Fix null to one-param default headers
        if (headers == null) {
            headers = new String[]{"User-Agent", "JDK11 Lightweight HttpClient"};
        }
        // Odd number of params -> Drop the last
        else if (headers.length % 2 != 0) {
            debug("nuber of params is odd: " + headers.length + " so the last will be dropped..");
            headers = Arrays.copyOf(headers, headers.length - 1);
        }

        return HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri))
                .setHeader("User-Agent", "JDK11 Lightweight HttpClient")
                .headers(headers)
                .build();
    }

    /**
     * Prepares simple Http GET Request with DEFAULT URI.
     *
     * @return prepared HTTP GET Request
     */
    public HttpRequest prepareGet() {
        try {
            return prepareGet(DEFAULT_URI);
        } catch (LackOfParameterException thrown) {
            error("Impossible situation happen in prepareGet -> already prepared param failed.");
        }
        return null;
    }

    /**
     * Prepares simple Http Client with HTTP ver 2.0 and timeout of 10 seconds.
     *
     * @return prepared HTTP client
     */
    public HttpClient prepareHttpClient() {
        // Possibly it was already initialized
        if (httpClient != null && !httpClient.isTerminated()) {
            return httpClient;
        }
        // Or needs to be initialized
        try {
            httpClient = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
        } catch (Exception thrown) {
            error("Couldn't rise HTTP Client due: ", thrown);
        }
        return httpClient;
    }

    // Log methods
    public static void info(String entry) {
        ILog.i(entry);
    }

    public static void error(String entry) {
        e(entry);
    }

    public static void error(String entry, Throwable thrown) {
        e(entry, thrown);
    }

    public static void debug(String entry) {
        if (IDebuggable.isInDebug()) {
            d(entry);
        }
    }

    // Debug methods
    public static void debugHeaders(HttpResponse<String> response) {
        if (IDebuggable.isInDebug() && response != null) {
            response.headers().map().forEach((k, v) -> d("Header: " + k + " => " + v));
        }
    }

    //System.out.println(response.statusCode());
    public static void debugStatusCode(HttpResponse<String> response) {
        if (IDebuggable.isInDebug() && response != null) {
            d("HTTP Response's Status Code: " + response.statusCode());
        }
    }

    public static void debugBody(HttpResponse<String> response) {
        if (IDebuggable.isInDebug() && response != null) {
            d("HTTP Response's body: " + response.body());
        }
    }

    public static void turnDebugOn() {
        try {
            IDebuggable.turnDebugOn();
        } catch (ForgotToImplementException e) {
            e("Couldn't turn on the DEBUG.");
        }
    }

    public static void turnDebugOff() {
        try {
            IDebuggable.turnDebugOff();
        } catch (ForgotToImplementException e) {
            e("Couldn't turn off the DEBUG.");
        }
    }

    // Constructors
    public static MicroRestClient getInstance() {
        if (_instance == null) {
            _instance = new MicroRestClient();
        }
        return _instance;
    }

    public static MicroRestClient getInstanceWithDebug() {
        if (_instance == null) {
            _instance = new MicroRestClient();
            turnDebugOn();
            debug("MicroRestClient has risen.");
        }
        return _instance;
    }

    /**
     * Hidden constructor as it should be a singleton.
     */
    private MicroRestClient() {
        this(DEFAULT_DEBUG);
    }

    public MicroRestClient(boolean debugFlag) {
        if (debugFlag) {
            turnDebugOn();
        }
    }

    // Destructors
    @Override
    public void kill() {
        if (httpClient != null) {
            if (!httpClient.isTerminated()) {
                httpClient.close();
            }
            httpClient = null;
        }
        info("MicroRestClient was killed.");
    }

    // Instances
    private static MicroRestClient _instance;

    private HttpClient httpClient = prepareHttpClient();

    // Default values
    private final static boolean DEFAULT_DEBUG = false;

    private final static String DEFAULT_URI = "https://api.chucknorris.io/jokes/random";

    private final static BodyHandler<String> DEFAULT_GET_RESPONSE_TYPE = HttpResponse.BodyHandlers.ofString();

}
