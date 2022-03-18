package main.java.DBObjects;


/**
 * Interface representing an instance of a table in the TrainHistory Database
 *
 *
 * @author Garrett Kamila Crayton
 * @version 1.0
 */
public interface DBObject
{
    /**
     * SQL INSERT statement representation
     *
     * @return the INSERT statement representation of the object (to be used to insert it into the database)
     */
    public String toSQL();
}