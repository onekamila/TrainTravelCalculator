package main.java.DBObjects;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


/**
 * Object representing an instance of the Stop table in the TrainHistory database
 *
 *
 * @author Garrett Kamila Crayton
 * @version 1.0
 */
public class Stop implements DBObject
{
    private String stationCode;
    private int trainID;
    private int stationID;
    private int routeID;
    private LocalDateTime sArr;
    private LocalDateTime sDep;
    private LocalDateTime aArr;
    private LocalDateTime aDep;


    /**
     * Class constructor
     *
     * @param stationCode the 3-letter code Amtrak uses to identify the station
     * @param sArr the scheduled date/time of arrival for the train
     * @param sDep the scheduled date/time of departure for the train
     * @param aArr the actual date/time of arrival for the train
     * @param aDep the actual date/time of departure for the train
     */
    public Stop(String stationCode, LocalDateTime sArr, LocalDateTime sDep, LocalDateTime aArr, LocalDateTime aDep)
    {
        this.stationCode = stationCode;
        this.sArr = sArr;
        this.sDep = sDep;
        this.aArr = aArr;
        this.aDep = aDep;
    }

    /**
     * Sets the ID number of the station from TrainHistory database
     *
     * @param stationID the station_id of the corresponding Station in the TrainHistory database
     */
    public void setStationID(int stationID)
    {
        this.stationID = stationID;
    }

    /**
     * Sets the ID number of the corresponding train from TrainHistory database
     *
     * @param trainID the train_id of the corresponding Station in the TrainHistory database
     */
    public void setTrainID(int trainID)
    {
        this.trainID = trainID;
    }

    /**
     * Set the ID number of the route from TrainHistory database
     *
     * @param routeID the route_id of the corresponding Station in the TrainHistory database
     */
    public void setRouteID(int routeID)
    {
        this.routeID = routeID;
    }

    /**
     * Returns the station code for this Stop
     *
     * @return the stationCode for this Stop
     */
    public String getStationCode()
    {
        return stationCode;
    }

    /**
     * <b>MAY NOT BE USED BY ANYTHING!!</b> (Need to confirm)<br>
     * String representation of this Stop (used for
     *
     * @return the String representation of this Stop
     */
    public String toString()
    {
        ArrayList<String> outArr = new ArrayList<>();


        outArr.add(getDateStr(sArr));
        outArr.add(getDateStr(sDep));
        outArr.add(getDateStr(aArr));
        outArr.add(getDateStr(aDep));


        return outArr.toString();
    }

    /**
     * Handles returning the String representation of the given LocalDateTime, or returns <code>null</code> if the given
     * value is <code>null</code>.
     * @param dateTime
     * @return
     */
    private String getDateStr(LocalDateTime dateTime)
    {
        String dateStr;
        try
        {
            dateStr = toDate(dateTime);
        }
        catch(NullPointerException e)
        {
            dateStr = "null";
        }

        return dateStr;
    }

    /**
     * SQL INSERT statement representation
     *
     * @return the INSERT statement representation of the Stop (to be used to insert it into the database)
     */
    public String toSQL()
    {
        String statement = "INSERT INTO stop VALUES (";
        statement += trainID + ", ";
        statement += stationID + ", ";
        statement += routeID + ", ";
        statement += dateCmd(sArr) + ", ";
        statement += dateCmd(sDep) + ", ";
        statement += dateCmd(aArr) + ", ";
        statement += dateCmd(aDep) + ")";

        return statement;
    }

    /**
     * SQL UPDATE statement representation
     *
     * @return the UPDATE statement representation of the Route (to be used to update it in the database)
     */
    public String toUpdate()
    {
        String statement = "UPDATE stop SET ";
        statement += " s_arrive = " + dateCmd(sArr) + ", ";
        statement += " s_depart = " + dateCmd(sDep) + ", ";
        statement += " a_arrive = " + dateCmd(aArr) + ", ";
        statement += " a_depart = " + dateCmd(aDep) + " ";
        statement += "WHERE (";
        statement += "train_id = " + trainID + ") AND (";
        statement += "station_id = " + stationID + ") AND (";
        statement += "route_id = " + routeID + ")";

        return statement;
    }

    /**
     * Converts the given LocalDateTime into the <code>to_date</code> SQL command needed to insert this Stop into the
     * TrainHistory database.
     * @param dateTime the LocalDateTime to be inserted
     * @return the <code>to_date</code> SQL command for this Stop
     */
    private String dateCmd(LocalDateTime dateTime)
    {
        String cmd = "";

        // Try to parse the given value
        try
        {
            cmd = "to_date('";
            cmd += toDate(dateTime);
            cmd += "', 'YYYY-MM-dd HH24:MI:SS')";
        }
        // If a NullPointerException is thrown, return "null"
        catch (NullPointerException e)
        {
            cmd = "null";
        }

        return cmd;
    }

    /**
     * String representation of the given LocalDateTime value
     *
     * @param dateTime the LocalDateTime value to be parsed
     * @return the String representation of the given LocalDateTime value
     * @throws NullPointerException if the given value is <code>null</code>
     */
    private String toDate(LocalDateTime dateTime) throws NullPointerException
    {
        String format = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(formatter);
    }
}