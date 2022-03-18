package main.java;


import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Handles the extraction phase of the ETL process and initializes the transform phase.
 *
 *
 *  @author Garrett Kamila Crayton
 *  @version 1.0
 */
public class ExtractMain
{
    /**
     * Class constructor
     */
    public ExtractMain()
    {
        ;
    }

    /**
     * Extract the data from <a href="https://statusmaps.com">statusmaps.com</a>
     *
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public void extract() throws URISyntaxException, IOException, InterruptedException
    {
        // Get change log
        String changeLog = getChangeLog();

        // Get changed files
        ArrayList<String> fileNames = getFileNames(changeLog);
    }

    /**
     * Get the change log for the previous day from <a href="https://statusmaps.com">statusmaps.com</a>
     *
     * @return the raw data from the HTTP request
     * @throws URISyntaxException if the syntax of the given URI is invalid
     * @throws IOException if an IO error occurs when sending or receiving
     * @throws InterruptedException if the operation is interrupted
     */
    private String getChangeLog() throws URISyntaxException, IOException, InterruptedException
    {
        ChangeLogRequest request = new ChangeLogRequest();
        HttpResponse<String> response = request.send();

        return response.body();
    }

    /**
     * TODO <b><u>THIS NEEDS TO BE RENAMED AND (eventually) REWRITTEN!!!!</u></b>
     * Parses the names of the files and passes them off to the TrainHistoryHandler.
     *
     * @param logFile the data from the ChangeLogRequest
     * @return a list of all the file names from the log file
     */
    private ArrayList<String> getFileNames(String logFile)
    {
        ArrayList<String> fileNames = new ArrayList<>();

        // Split the log file data by line
        ArrayList<String> lines = new ArrayList<String>(Arrays.asList(logFile.split("\n")));

        System.out.println("Log files to request: " + lines.size());

        // To help limit the strain on statusmaps.com
        int threads = 0;

        // Loop through each line
        for(String line: lines)
        {
            // Split the line by space
            String[] lineArr = line.strip().split(" ");

            // If this is the last line, continue (loop will exit)
            if(lineArr[5].equals("Finished"))
                continue;

            // Get the filename
            String fileName = lineArr[6];

            // If the filename hasn't already been found
            if(!fileNames.contains(fileName))
            {
                // Add the filename to the list of files
                fileNames.add(fileName);

                // Pass the filename to a new thread for further extraction, transformation, and loading
                TrainHistoryHandler handler = new TrainHistoryHandler(fileName);
                handler.start();

                try
                {
                    // Increment the number of threads
                    threads++;

                    // Every 50 threads, wait 500ms to reduce strain on statusmaps.com (and suspicion on the system)
                    if((threads % 50) == 0)
                    {
                        System.out.println("Pausing to reduce strain on the server...");
                        Thread.sleep(500);
                    }
                }
                // Just in case something happens
                catch (InterruptedException i)
                {
                    ;
                }
            }
        }

        return fileNames;
    }
}