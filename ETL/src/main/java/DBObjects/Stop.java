package main.java.DBObjects;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


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


    public Stop(String stationCode, LocalDateTime sArr, LocalDateTime sDep, LocalDateTime aArr, LocalDateTime aDep)
    {
        this.stationCode = stationCode;
        this.sArr = sArr;
        this.sDep = sDep;
        this.aArr = aArr;
        this.aDep = aDep;
    }

    public void setStationID(int stationID)
    {
        this.stationID = stationID;
    }

    public void setTrainID(int trainID)
    {
        this.trainID = trainID;
    }

    public void setRouteID(int routeID)
    {
        this.routeID = routeID;
    }

    public String getStationCode()
    {
        return stationCode;
    }

    public String toString()
    {
        ArrayList<String> outArr = new ArrayList<>();


        outArr.add(getDateStr(sArr));
        outArr.add(getDateStr(sDep));
        outArr.add(getDateStr(aArr));
        outArr.add(getDateStr(aDep));


        return outArr.toString();
    }

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

    private String dateCmd(LocalDateTime dateTime)
    {
        String cmd = "";
        try
        {
            cmd = "to_date('";
            cmd += toDate(dateTime);
            cmd += "', 'YYYY-MM-dd HH24:MI:SS')";
        }
        catch (NullPointerException e)
        {
            cmd = "null";
        }

        return cmd;
    }

    private String toDate(LocalDateTime dateTime) throws NullPointerException
    {
        String format = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(formatter);
    }
}