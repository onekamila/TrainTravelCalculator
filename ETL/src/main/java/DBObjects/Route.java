package main.java.DBObjects;


public class Route implements DBObject
{
    private int routeID;
    private String routeName;


    public Route(int routeID, String routeName)
    {
        this.routeID = routeID;
        String cleanName = routeName.replaceAll("\\*", " ").strip();
        this.routeName = cleanName;
    }


    public String toSQL()
    {
        String sql = "INSERT INTO route VALUES (";
        sql += routeID + ", '";
        sql += routeName + "')";
        return sql;
    }
}