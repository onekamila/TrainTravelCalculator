package main.java;


import main.java.DBObjects.Route;
import main.java.DBObjects.Stop;
import main.java.DBObjects.Train;

import java.net.ConnectException;
import java.net.http.HttpResponse;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Handles the extraction, transformation, and loading of a single train history log.
 *
 *
 *  @author Garrett Kamila Crayton
 *  @version 1.0
 */
public class TrainHistoryHandler extends Thread
{
    private String fileName;
    private int attempts;
    private TransformMain transform;
    private DBConnection db;


    /**
     * Class constructor
     *
     * @param fileName the name of the train history log
     */
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

    /**
     * Runs the handling of train history data
     */
    @Override
    public void run()
    {
        try
        {
            // Create a new request for the appropriate train log
            TrainHistoryRequest request = new TrainHistoryRequest(fileName);

            // Send the request
            HttpResponse<String> response = request.send();

            // If the request is successful, parse the data
            if(response.statusCode() == 200)
            {
                // Pull the data from the response body
                String rawData = response.body();

                // Get the route name from the data
                String routeName = rawData.split("\n")[0];

                // get the route ID from the TrainHistory database
                int routeID = getRouteID(routeName);

                // Parse the Train object from the file name
                Train train = transform.parseTrain(fileName);

                // Get the train's ID number
                int trainID = getTrainID(train);

                // Clean the stop data
                ArrayList<Stop> cleanedData = transform.cleanHistoryData(fileName, rawData);

                // Insert all the stop data
                insertStops(routeID, trainID, cleanedData);
            }
            // If the request fails, explain why
            else
            {
                System.out.println("Error: " + response.statusCode() + " Status Code received for: " + fileName);
            }
        }
        // In case the connection resets
        catch(ConnectException e)
        {
            // If there haven't been too many attempts already
            if(attempts > 0)
            {
                // Wait then try again
                try
                {
                    System.out.println("Connection timed out for: " + fileName + "(" + attempts + "); Reattempting...");
                    Thread.sleep(200);
                    attempts--;
                    run();
                }
                // Just in case something weird happens
                catch (InterruptedException i)
                {
                    i.printStackTrace();
                }
            }
            // If no more attempts, say so and exit
            else
            {
                System.out.println("Error: issue with connection for: " + fileName);
            }
        }
        // Just in case something weird happens
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // Close the connection
        try
        {
            db.close();
        }
        // Just in case something weird happens
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get the route_id for the route from TrainHistory database
     *
     * @param routeName the name of the route
     * @return the route_id for the route
     */
    private int getRouteID(String routeName)
    {
        try
        {
            // Clean the route name
            routeName = routeName.replaceAll("\\*", " ").strip();

            // Build the query
            String query = "SELECT route_id FROM route WHERE route_name = '" + routeName + "'";

            // Query the TrainHistory database
            ResultSet result = db.query(query);

            // If a result is returned, return it
            if(result.next())
                return result.getInt(1);
            else
            {
                // Get the next route_id from the TrainHistory data
                query = "SELECT route_id_sequence.nextval FROM DUAL";

                // Send the query to the TrainHistory database
                result = db.query(query);

                // Get the next result
                result.next();

                // Return the next result (if there is one)
                int routeID = result.getInt(1);

                // Create the Route object
                Route route = new Route(routeID, routeName);

                // Insert the Route into the TrainHistory database
                db.insert(route);

                // Return the routeID
                return routeID;
            }
        }
        // Just in case something weird happens
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Get the train_id for the Train from TrainHistory database
     *
     * @param train the name of the train
     * @return the train_id for the train
     */
    private int getTrainID(Train train)
    {
        ResultSet result;
        try
        {
            // Convert the train to a query
            String query = train.toQuery();

            // Send the query to the TrainHistory database
            result = db.query(query);

            // Get the next result
            result.next();

            // Return the next result (if there is one)
            return result.getInt(1);
        }
        // If there isn't a result (i.e. no train), or if something weird happens)
        catch (SQLException e)
        {
            try
            {
                // Get next available train_id from the TrainHistory database
                result = db.query("SELECT train_id_sequence.nextval FROM DUAL");

                // Get the next result
                result.next();

                // Get the next train_id from the result
                int trainID = result.getInt(1);

                // Update the Train
                train.setID(trainID);

                // Insert the Train into the TrainHistory database
                db.insert(train);

                // Return the new trainID
                return trainID;
            }
            // Just in case something weird happens
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        }

        return 0;
    }

    /**
     * Get the station_id for the station from TrainHistory database
     *
     * @param stnCode the station code of the station
     * @return the station_id for the station
     */
    private int getStationID(String stnCode)
    {
        try
        {
            String query = "SELECT station_id FROM station WHERE code = '" + stnCode + "'";
            ResultSet result = db.query(query);

            result.next();
            return result.getInt(1);
        }
        // Just in case something weird happens
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Insert the stops into the TrainHistory database
     *
     * @param routeID the route_id of the route
     * @param trainID the train_id of the route
     * @param stops the list of stops to be inserted
     * @throws SQLException if an error occurs during the operation
     */
    private void insertStops(int routeID, int trainID, ArrayList<Stop> stops) throws SQLException
    {
        for(Stop stop: stops)
        {
            int stationID = getStationID(stop.getStationCode());

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