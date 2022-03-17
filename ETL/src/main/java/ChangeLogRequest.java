package main.java;

import java.net.URISyntaxException;
import java.time.LocalDate;

public class ChangeLogRequest extends Request
{
    public ChangeLogRequest() throws URISyntaxException
    {
        super(null);
    }

    protected String getFullURL(String url)
    {
        String fullURL = getFullBaseURL();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(2);

        String dateStr = yesterday.toString();
        String date = dateStr.replaceAll("-", "");

        fullURL += "logs/changed_trains_" + date + ".log";

        return fullURL;
    }
}
