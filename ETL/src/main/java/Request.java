package main.java;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;


public abstract class Request
{
    protected static final String BASE_URL = "http://www.dixielandsoftware.net/Amtrak/status/archive/";

    private HttpClient client;
    private HttpRequest request;


    public Request(String url) throws URISyntaxException
    {
        client = HttpClient.newHttpClient();
        String fullURL = getFullURL(url);
        URI uri = new URI(fullURL);
        request = HttpRequest.newBuilder().uri(uri).GET().build();
    }


    protected abstract String getFullURL(String url);

    protected String getFullBaseURL()
    {
        String fullURL = BASE_URL;

        int currentYear = LocalDate.now().getYear();

        fullURL += currentYear + "/";

        return fullURL;
    }

    public HttpResponse<String> send() throws IOException, InterruptedException
    {
        return client.send(request, BodyHandlers.ofString());
    }
}