package main.java;


import java.net.URISyntaxException;


/**
 * Representation of a request that will be sent to <a href="http://www.statusmaps.com/">statusmaps.com</a> for a
 * single train history log.
 *
 *
 *  @author Garrett Kamila Crayton
 *  @version 1.0
 */
public class TrainHistoryRequest extends Request
{
    /**
     * Class constructor
     *
     * @param fileName the name of the desired file
     * @throws URISyntaxException if the syntax of the given URI is invalid
     */
    public TrainHistoryRequest(String fileName) throws URISyntaxException
    {
        super(fileName);
    }


    /**
     * Returns the full URL path to retrieve the log file
     *
     * @param filename the URL path to the desired file
     * @return the full URL address for the desired file
     */
    protected String getFullURL(String filename)
    {
        String fullURL = getFullBaseURL();
        int trainNum = parseTrainNum(filename);

        fullURL += trainNum + "/" + filename;

        return fullURL;
    }

    /**
     * Return the train number based on the filename given
     *
     * @param fileName the filename to be parsed
     * @return the train number from the filename
     */
    private int parseTrainNum(String fileName)
    {
        String[] fileArr = fileName.split("_");
        return Integer.parseInt(fileArr[0]);
    }
}