package main.java.DBObjects;


/**
 * Object representing an instance of the Route table in the TrainHistory database
 *
 *
 * @author Garrett Kamila Crayton
 * @version 1.0
 */
public class Route implements DBObject
{
    private int routeID;
    private String routeName;


    /**
     * Class constructor
     *
     * @param routeID the ID number of the route (to be retrieved from TrainHistory DB)
     * @param routeName the name of the route
     */
    public Route(int routeID, String routeName)
    {
        this.routeID = routeID;
        String cleanName = routeName.replaceAll("\\*", " ").strip();
        this.routeName = cleanName;
    }

    /**
     * SQL INSERT statement representation
     *
     * @return the INSERT statement representation of the Route (to be used to insert it into the database)
     */
    public String toSQL()
    {
        String sql = "INSERT INTO route VALUES (";
        sql += routeID + ", '";
        sql += routeName + "')";
        return sql;
    }
}