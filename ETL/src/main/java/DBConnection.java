package main.java;


import main.java.DBObjects.DBObject;

import java.sql.*;


/**
 * Object that manages the connection between the ETL system and the TrainHistory database
 *
 *
 *  @author Garrett Kamila Crayton
 *  @version 1.0
 */
public class DBConnection
{
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/TrainHistory";
    private static final String USR = "etl";
    private static final String PSWD = "Amtrak315";
    private Connection dbConn;


    /**
     * Class constructor
     *
     * @throws SQLException if a database access error occurs
     */
    public DBConnection() throws SQLException
    {
        dbConn = DriverManager.getConnection(URL, USR, PSWD);
    }


    /**
     * Inserts the given object into the TrainHistory database
     *
     * @param dbo the object to be inserted
     * @throws SQLException if an error occurs during the insert
     */
    public void insert(DBObject dbo) throws SQLException
    {
        Statement stmt = dbConn.createStatement();

        stmt.executeQuery(dbo.toSQL());
    }

    /**
     * Sends the given query to the TrainHistory database and returns the result
     *
     * @param queryStr the query to be sent
     * @return the result of the given query
     * @throws SQLException if an error occurs during the query
     */
    public ResultSet query(String queryStr) throws SQLException
    {
        Statement query = dbConn.createStatement();
        return query.executeQuery(queryStr);
    }


    /**
     * Close the connection with the database
     *
     * @throws SQLException if a database access error occurs
     */
    public void close() throws SQLException
    {
        dbConn.close();
    }
}