package main.java;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;

public class ExtractMain
{
    public ExtractMain()
    {
        ;
    }

    public void extract() throws URISyntaxException, IOException, InterruptedException
    {
        // Get change log
        String changeLog = getChangeLog();

        // Get changed files
        ArrayList<String> fileNames = getFileNames(changeLog);
    }


    private String getChangeLog() throws URISyntaxException, IOException, InterruptedException
    {
        ChangeLogRequest request = new ChangeLogRequest();
        HttpResponse<String> response = request.send();

        return response.body();
    }

    private ArrayList<String> getFileNames(String logFile)
    {
        ArrayList<String> fileNames = new ArrayList<>();
        ArrayList<String> lines = new ArrayList<String>(Arrays.asList(logFile.split("\n")));

        System.out.println("Log files to request: " + lines.size());

        int threads = 0;
        for(String line: lines)
        {
            String[] lineArr = line.strip().split(" ");
            if(lineArr[5].equals("Finished"))
                continue;

            String fileName = lineArr[6];
            if(!fileNames.contains(fileName))
            {
                fileNames.add(fileName);
                TrainHistoryHandler handler = new TrainHistoryHandler(fileName);
                handler.start();

                try
                {
                    threads++;
                    if((threads % 50) == 0)
                    {
                        System.out.println("Pausing to reduce strain on the server...");
                        Thread.sleep(500);
                    }
                }
                catch (Exception i)
                {
                    ;
                }
            }
        }

        return fileNames;
    }
}
