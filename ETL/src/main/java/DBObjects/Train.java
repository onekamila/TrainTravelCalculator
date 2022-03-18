package main.java.DBObjects;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * Object representing an instance of the Train table in the TrainHistory database
 *
 *
 * @author Garrett Kamila Crayton
 * @version 1.0
 */
public class Train implements DBObject
{
    /**
     * The ID of the train within the TrainHistory database
     */
    private int id;
    /**
     * The number of the train (corresponds to the route and schedule of the train)
     */
    private int num;
    /**
     * The date the train departed from its origin station
     */
    private LocalDate depart;


    /**
     * Class Constructor
     *
     * @param num the train number of this train
     * @param depart the date the train departed from its origin station
     */
    public Train(int num, LocalDate depart)
    {
        this.num = num;
        this.depart = depart;
    }


    /**
     * Sets the ID of this train within the TrainHistory database
     * @param id
     */
    public void setID(int id)
    {
        this.id = id;
    }


    /**
     * SQL INSERT statement representation
     *
     * @return the INSERT statement representation of the Train (to be used to insert it into the database)
     */
    public String toSQL()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String sql = "INSERT INTO train VALUES (";
        sql += id + ", ";
        //sql += "train_id_sequence.NEXTVAL, ";
        sql += num + ", to_date('";
        sql += depart.format(formatter);
        sql += "', 'YYYY-MM-DD'))";

        return sql;
    }

    /**
     * SQL query representation
     *
     * @return the query representation of the Route (to get the corresponding train_id from the database)
     */
    public String toQuery()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String sql = "SELECT train_id FROM train WHERE train_number =";
        sql += num + " AND (";
        sql += "depart_date = to_date('" + depart.format(formatter);
        sql += "', 'YYYY-MM-DD'))";

        return sql;
    }
}