package main.java;

import main.java.DBObjects.DBObject;

import java.sql.*;

public class DBConnection
{
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/TrainHistory";
    private static final String USR = "etl";
    private static final String PSWD = "Amtrak315";
    private Connection dbConn;

    public DBConnection() throws SQLException
    {
        dbConn = DriverManager.getConnection(URL, USR, PSWD);
    }


    public void insert(DBObject dbo) throws SQLException
    {
        Statement stmt = dbConn.createStatement();

        stmt.executeQuery(dbo.toSQL());
    }


    public ResultSet query(String queryStr) throws SQLException
    {
        Statement query = dbConn.createStatement();
        return query.executeQuery(queryStr);
    }

    public void close() throws SQLException
    {
        dbConn.close();
    }
}
