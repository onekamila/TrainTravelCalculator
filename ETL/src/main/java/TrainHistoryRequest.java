package main.java;

import java.net.URISyntaxException;

public class TrainHistoryRequest extends Request
{
    public TrainHistoryRequest(String fileName) throws URISyntaxException
    {
        super(fileName);
    }

    protected String getFullURL(String url)
    {
        String fullURL = getFullBaseURL();
        int trainNum = parseTrainNum(url);

        fullURL += trainNum + "/" + url;

        return fullURL;
    }

    private int parseTrainNum(String fileName)
    {
        String[] fileArr = fileName.split("_");
        return Integer.parseInt(fileArr[0]);
    }
}
