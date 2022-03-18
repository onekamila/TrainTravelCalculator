package main.java;


import main.java.DBObjects.Stop;
import main.java.DBObjects.Train;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * Handles the transformation of the data received from <a href="https://www.statusmaps.com/">statusmaps.com</a> to be
 * loaded into the TrainHistory database.
 *
 *
 *  @author Garrett Kamila Crayton
 *  @version 1.0
 */
public class TransformMain
{
    /**
     * The date and time the train departed from its origin station
     */
    private LocalDateTime originDate;


    /**
     * Class constructor
     */
    public TransformMain()
    {

    }


    /**
     * Parse the raw data from <a href="https://www.statusmaps.com/">statusmaps.com</a> into a list of Stop objects.
     *
     * @param fileName the name of the train log retrieved from <a href="https://www.statusmaps.com/">statusmaps.com</a>
     * @param data the raw data from the retrieved train log
     * @return a list of Stop objects parsed from the datalog
     */
    public ArrayList<Stop> cleanHistoryData(String fileName, String data)
    {
        ArrayList<Stop> stops = new ArrayList<>();

        // Get date
        parseOriginDate(fileName);

        // Remove headers
        ArrayList<String> lines = removeHeaders(data);

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

    /**
     * Returns the date the train departed from its origin station
     *
     * @param fileName the name of the train log retrieved from <a href="https://www.statusmaps.com/">statusmaps.com</a>
     */
    private void parseOriginDate(String fileName)
    {
        // Get the date string from the filename
        String[] nameArr = fileName.split("[_.]");
        String dateStr = nameArr[1];

        // Parse the datetime value from the String value
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
        originDate = LocalDate.parse(dateStr, format).atStartOfDay();
    }

    /**
     * Removes the header from the train log retrieved from <a href="https://www.statusmaps.com/">statusmaps.com</a>
     *
     * @param data the raw data from the retrieved train log
     * @return the data with the headers removed
     */
    private ArrayList<String> removeHeaders(String data)
    {
        // Split the data into lines
        String[] dataLines = data.split("\n");
        ArrayList<String> lines = new ArrayList<>(Arrays.asList(dataLines));

        // Remove headers
        lines.removeAll(lines.subList(0, 2));    // Remove the first two rows (both contain irrelevant information)
        lines.removeIf(line -> (line.contains("+")));
        lines.removeIf(line -> (line.contains("V")));
        lines.removeIf(line -> (line.contains("Eng: "))); // Used to remove any engine data. Would like to track this eventually

        return lines;
    }

    /**
     * Parse a single line from the train log retrieved from <a href="https://www.statusmaps.com/">statusmaps.com</a>
     * into a Stop object
     *
     * @param rawLine the raw line of data from the retrieved train log
     * @return the Stop object represented by the line data
     */
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

        // Create new stop object
        Stop stop = new Stop(rawVals.get(0), cleanedDates.get(0), cleanedDates.get(1), cleanedDates.get(2), cleanedDates.get(3));

        return stop;
    }

    /**
     * Parse the list of String values into a list of LocalDateTime values corresponding to the given values
     *
     * @param strVals the list String values of each DateTime value
     * @return list of LocalDateTime values corresponding to the given String values
     */
    private ArrayList<LocalDateTime> parseDates(List<String> strVals)
    {
        ArrayList<LocalDateTime> dateVals = new ArrayList<>();

        // Get the appropriate integer values from the given String values
        ArrayList<Integer> intVals = parseDateInts(strVals);


        // Loop through each pair of dateVals
        LocalDateTime dateTime;
        for(int i = 0; i < 8; i += 2)
        {
            // Try to parse the current value
            try
            {
                dateTime = parseDateTime(intVals.get(i), intVals.get(i + 1));
            }
            // If the current value was not given, return null
            catch (NullPointerException e)
            {
                dateTime = null;
            }

            // Add the current value to the list of dateVals
            dateVals.add(dateTime);
        }

        return dateVals;
    }

    /**
     * Parse the given list of String values into a list of integers corresponding to the entry's date
     *
     * @param rawVals the list of String values
     * @return a list of corresponding Integers
     */
    private ArrayList<Integer> parseDateInts(List<String> rawVals)
    {
        // Parse ints
        ArrayList<Integer> intVals = parseInts(rawVals);

        // Replace missing values
        if(intVals.get(0) == null)
            intVals.set(0, intVals.get(2));
        if(intVals.get(1) == null)
            intVals.set(1, intVals.get(3) - 1);
        if(intVals.get(4) == null)
            intVals.set(4, intVals.get(5) - 1);

        // Insert missing values
        intVals.add(5, intVals.get(2));
        intVals.add(4, intVals.get(0));

        // Increment days of actual arrival/departure accordingly
        if(intVals.get(1) > intVals.get(5))
            intVals.set(4, (intVals.get(4) + 1));
        if((intVals.get(3) != null) && (intVals.get(7) != null))
            if(intVals.get(3) > intVals.get(7))
                intVals.set(6, (intVals.get(6) + 1));

        return intVals;
    }

    /**
     * Parse the given list of String values into a list of integers
     *
     * @param rawStrs the list of String values
     * @return a list of corresponding Integers
     */
    private ArrayList<Integer> parseInts(List<String> rawStrs)
    {
        ArrayList<Integer> intVals = new ArrayList<>();

        // Loop through the list of raw strings
        for(String strVal: rawStrs)
        {
            // If the current value is null, return null
            if(strVal == null)
                intVals.add(null);
            else
            {
                int timeInt;

                // If the current value contains "A" or "P", treat as a time integer
                if(strVal.contains("A") || strVal.contains("P"))
                {
                    // Parse the actual integer
                    int len = strVal.length();
                    String timeStr = strVal.substring(0, len-1);
                    timeInt = Integer.parseInt(timeStr);

                    // Parse the time of day
                    char type = strVal.charAt(len-1);

                    // If the time of day is afternoon/evening ("P"), add 12 hours
                    if(type == 'P')
                        timeInt += 1200;
                }
                // If the current value doesn't contain "A" or "P", treat as a day value;
                else
                {
                    timeInt = Integer.parseInt(strVal);
                }

                // Add the value to the output array
                intVals.add(timeInt);
            }
        }

        return intVals;
    }

    /**
     * Convert the given values into a LocalDateTime
     *
     * @param days the number of days after the train's departure
     * @param time the time of day
     * @return LocalDateTime corresponding to the given values
     */
    private LocalDateTime parseDateTime(int days, int time)
    {
        // Increment by the number of days
        LocalDateTime dateTime = originDate.plusDays(days); // Might cause incorrect data...

        // Parse hours (first two digits)
        dateTime = dateTime.plusHours(time / 100);

        // Parse minutes (last two digits
        dateTime = dateTime.plusMinutes(time % 100);

        return dateTime;
    }

    /**
     * Parse the Train object based on the given file name
     *
     * @param fileName the name of the train log
     * @return Train object based on the given file name
     */
    public Train parseTrain(String fileName)
    {
        // Split the filename
        String[] fileArr = fileName.split("[_.]");

        // Parse the train number
        int trainNum = Integer.parseInt(fileArr[0]);

        // Parse the date integer value
        int dateInt = Integer.parseInt(fileArr[1]);

        // Parse the LocalDate value of the integer value
        LocalDate departDate = LocalDate.of(dateInt / 10000, (dateInt % 10000) / 100, dateInt % 100);

        return new Train(trainNum, departDate);
    }
}