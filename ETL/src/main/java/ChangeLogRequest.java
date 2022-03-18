package main.java;


import java.net.URISyntaxException;
import java.time.LocalDate;


/**
 * Sends a request to <a href="https://statusmaps.com">statusmaps.com</a> for the previous day's change log.
 *
 *
 *  @author Garrett Kamila Crayton
 *  @version 1.0
 */
public class ChangeLogRequest extends Request
{
    /**
     * Class constructor
     *
     * @throws URISyntaxException if the syntax of the given URI is invalid
     */
    public ChangeLogRequest() throws URISyntaxException
    {
        super(null);
    }

    /**
     * Returns the full URL path to retrieve the log file
     *
     * @param fileName the URL path to the desired file
     * @return the full URL address for the desired file
     */
    protected String getFullURL(String fileName)
    {
        // Get the base URL for statusmaps.com
        String fullURL = getFullBaseURL();

        // Calculate yesterday's date
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        // Format yesterday's date appropriately
        String dateStr = yesterday.toString();
        String date = dateStr.replaceAll("-", "");

        //
        fullURL += "logs/changed_trains_" + date + ".log";

        return fullURL;
    }
}