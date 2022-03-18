package main.java;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;


/**
 * Representation of an HTTP request sent to <a href="http://www.statusmaps.com/">statusmaps.com</a>
 *
 *
 *  @author Garrett Kamila Crayton
 *  @version 1.0
 */
public abstract class Request
{
    protected static final String BASE_URL = "http://www.dixielandsoftware.net/Amtrak/status/archive/";

    private HttpClient client;
    private HttpRequest request;


    /**
     * Class constructor
     *
     * @param fileName the name of the desired file
     * @throws URISyntaxException if the syntax of the given URI is invalid
     */
    public Request(String fileName) throws URISyntaxException
    {
        client = HttpClient.newHttpClient();
        String fullURL = getFullURL(fileName);
        URI uri = new URI(fullURL);
        request = HttpRequest.newBuilder().uri(uri).GET().build();
    }


    /**
     * Returns the full URL path to retrieve the log file
     *
     * @param filename the URL path to the desired file
     * @return the full URL address for the desired file
     */
    protected abstract String getFullURL(String filename);

    /**
     * Returns the full base URL to <a href="http://www.statusmaps.com/">statusmaps.com</a>
     * @return the full base URl
     */
    protected String getFullBaseURL()
    {
        String fullURL = BASE_URL;

        int currentYear = LocalDate.now().getYear();

        fullURL += currentYear + "/";

        return fullURL;
    }

    /**
     * Sends this request to <a href="http://www.statusmaps.com/">statusmaps.com</a>
     *
     * @return the response to the request
     * @throws IOException if an IO error occurs when sending or receiving
     * @throws InterruptedException if the operation is interrupted
     */
    public HttpResponse<String> send() throws IOException, InterruptedException
    {
        return client.send(request, BodyHandlers.ofString());
    }
}