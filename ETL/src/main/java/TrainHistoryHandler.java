package main.java;


import main.java.DBObjects.Route;
import main.java.DBObjects.Stop;
import main.java.DBObjects.Train;

import java.net.ConnectException;
import java.net.http.HttpResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class TrainHistoryHandler extends Thread
{
    private String fileName;
    private int attempts;
    private TransformMain transform;
    private DBConnection db;


    public TrainHistoryHandler(String fileName)
    {
        attempts = 2;
        this.fileName = fileName;
        transform = new TransformMain();
        try
        {
            db = new DBConnection();
        }
        catch (Exception e)
        {
            ;
        }
    }

    public void run()
    {
        try
        {
            TrainHistoryRequest request = new TrainHistoryRequest(fileName);
            HttpResponse<String> response = request.send();
            if(response.statusCode() == 200)
            {
                String rawData = response.body();

                String routeName = rawData.split("\n")[0];
                ArrayList<Stop> cleanedData = transform.cleanHistoryData(fileName, rawData);

                int routeID = getRouteID(routeName);


                Train train = transform.parseTrain(fileName);
                int trainID = getTrainID(train);
                train.setID(trainID);

                try
                {
                    db.insert(train);
                }
                catch (SQLException e)
                {
                    ;
                }

                insertStops(routeID, trainID, cleanedData);
            }
            else
            {
                System.out.println("Error: " + response.statusCode() + " Status Code received for: " + fileName);
            }
        }
        catch(ConnectException e)
        {
            if(attempts > 0)
            {
                try
                {
                    System.out.println("Connection timed out for: " + fileName + "(" + attempts + "); Reattempting...");
                    Thread.sleep(200);
                    attempts--;
                    run();
                }
                catch (InterruptedException i)
                {
                    i.printStackTrace();
                }

            }
            else
            {
                System.out.println("Error: issue with connection for: " + fileName);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            db.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private int getRouteID(String routeName)
    {
        try
        {
            routeName = routeName.replaceAll("\\*", " ").strip();
            String query = "SELECT route_id FROM route WHERE route_name = '" + routeName + "'";

            ResultSet result = db.query(query);
            if(result.next())
                return result.getInt(1);
            else
            {
                query = "SELECT route_id_sequence.nextval FROM DUAL";
                result = db.query(query);
                result.next();
                int routeID = result.getInt(1);
                Route route = new Route(routeID, routeName);
                db.insert(route);

                return routeID;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

    private int getTrainID(Train train)
    {
        try
        {
            String query = train.toQuery();
            ResultSet result = db.query(query);
            result.next();
            return result.getInt(1);
        }
        catch (SQLException e)
        {
            try
            {
                ResultSet result = db.query("SELECT train_id_sequence.nextval FROM DUAL");
                result.next();
                return result.getInt(1);
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        }

        return 0;
    }

    private int getStationID(String stnCode)
    {
        try
        {
            String query = "SELECT station_id FROM station WHERE code = '" + stnCode + "'";
            ResultSet result = db.query(query);
            result.next();
            return result.getInt(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

    private void insertStops(int routeID, int trainID, ArrayList<Stop> stops) throws SQLException
    {
        for(Stop stop: stops)
        {
            String stationCode = stop.getStationCode();
            int stationID = getStationID(stationCode);

            stop.setTrainID(trainID);
            stop.setStationID(stationID);
            stop.setRouteID(routeID);

            try
            {
                db.insert(stop);
            }
            catch (SQLException e)
            {
                db.query(stop.toUpdate());
            }
        }
    }
}