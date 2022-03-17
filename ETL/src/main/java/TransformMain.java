package main.java;


import main.java.DBObjects.Stop;
import main.java.DBObjects.Train;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class TransformMain
{
    private LocalDateTime originDate;


    public TransformMain()
    {

    }


    public ArrayList<Stop> cleanHistoryData(String fileName, String data)
    {
        ArrayList<Stop> stops = new ArrayList<>();

        // Get date
        parseOriginDate(fileName);

        // Remove headers
        ArrayList<String> lines = removeHeaders(data);

        // Store first index (route name)
        String routeName = lines.remove(0);

        // Loop through each line
        for(String line: lines)
        {
            if((line.charAt(0) != '*') || (line.toLowerCase().contains("canceled")))
                continue;

            // Clean line
            stops.add(parseStop(line));
        }

        return stops;
    }

    private void parseOriginDate(String fileName)
    {
        String[] nameArr = fileName.split("[_.]");
        String dateStr = nameArr[1];

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
        originDate = LocalDate.parse(dateStr, format).atStartOfDay();
    }

    private ArrayList<String> removeHeaders(String data)
    {
        String[] dataLines = data.split("\n");
        ArrayList<String> lines = new ArrayList<>(Arrays.asList(dataLines));


        lines.removeIf(line -> (line.contains("+")));
        lines.removeIf(line -> (line.contains("V")));
        lines.removeIf(line -> (line.contains("Eng: "))); // Used to remove any engine data. Would like to track this eventually

        return lines;
    }

    private Stop parseStop(String rawLine)
    {
        rawLine = rawLine.replaceAll("\s+", " ");

        // Split line by space
        ArrayList<String> allRawVals = new ArrayList<>(Arrays.asList(rawLine.strip().split(" ")));

        // Remove first index + comment indecies
        // Throws error for engine details
        ArrayList<String> rawVals = new ArrayList<String>(allRawVals.subList(1, 8));

        // Replace all '*' w/ null
        Collections.replaceAll(rawVals, "*", null);


        // Parse dates
        ArrayList<LocalDateTime> cleanedDates = parseDates(rawVals.subList(1, rawVals.size()));

        Stop stop = new Stop(rawVals.get(0), cleanedDates.get(0), cleanedDates.get(1), cleanedDates.get(2), cleanedDates.get(3));

        return stop;
    }

    private ArrayList<LocalDateTime> parseDates(List<String> strVals)
    {
        ArrayList<LocalDateTime> dateVals = new ArrayList<>();
        ArrayList<Integer> intVals = parseDateInts(strVals);

        LocalDateTime dateTime;
        for(int i = 0; i < 8; i += 2)
        {
            try
            {
                dateTime = parseDateTime(intVals.get(i), intVals.get(i + 1));
            }
            catch (NullPointerException e)
            {
                dateTime = null;
            }
            dateVals.add(dateTime);
        }

        return dateVals;
    }

    private ArrayList<Integer> parseDateInts(List<String> rawVals)
    {
        // Parse ints
        ArrayList<Integer> intVals = parseInts(rawVals);

        if(intVals.get(0) == null)
            intVals.set(0, intVals.get(2));
        if(intVals.get(1) == null)
            intVals.set(1, intVals.get(3) - 1);
        if(intVals.get(4) == null)
            intVals.set(4, intVals.get(5) - 1);

        intVals.add(5, intVals.get(2));
        intVals.add(4, intVals.get(0));

        if(intVals.get(1) > intVals.get(5))
            intVals.set(4, (intVals.get(4) + 1));
        if((intVals.get(3) != null) && (intVals.get(7) != null))
            if(intVals.get(3) > intVals.get(7))
                intVals.set(6, (intVals.get(6) + 1));

        return intVals;
    }

    private ArrayList<Integer> parseInts(List<String> rawStrs)
    {
        ArrayList<Integer> intVals = new ArrayList<>();

        for(String strVal: rawStrs)
        {
            if(strVal == null)
                intVals.add(null);
            else
            {
                int timeInt;
                if(strVal.contains("A") || strVal.contains("P"))
                {
                    int len = strVal.length();
                    String timeStr = strVal.substring(0, len-1);
                    timeInt = Integer.parseInt(timeStr);
                    char type = strVal.charAt(len-1);

                    if(type == 'P')
                        timeInt += 1200;
                }
                else
                {
                    timeInt = Integer.parseInt(strVal);
                }

                intVals.add(timeInt);
            }
        }

        return intVals;
    }

    private LocalDateTime parseDateTime(int days, int time)
    {
        LocalDateTime dateTime = originDate.plusDays(days);
        dateTime = dateTime.plusHours(time / 100);
        dateTime = dateTime.plusMinutes(time % 100);

        return dateTime;
    }

    public Train parseTrain(String fileName)
    {
        String[] fileArr = fileName.split("[_.]");
        int trainNum = Integer.parseInt(fileArr[0]);
        int dateInt = Integer.parseInt(fileArr[1]);
        LocalDate departDate = LocalDate.of(dateInt / 10000, (dateInt % 10000) / 100, dateInt % 100);
        return new Train(trainNum, departDate);
    }
}