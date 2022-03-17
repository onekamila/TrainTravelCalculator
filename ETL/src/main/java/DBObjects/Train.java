package main.java.DBObjects;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Train implements DBObject
{
    private int id;
    private int num;
    private LocalDate depart;

    public Train(int num, LocalDate depart)
    {
        this.num = num;
        this.depart = depart;
    }

    public void setID(int id)
    {
        this.id = id;
    }

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

    public String toQuery()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String sql = "SELECT train_id FROM train WHERE train_number =";
        sql += num + " AND to_date('";
        sql += depart.format(formatter);
        sql += "', 'YYYY-MM-DD') = depart_date";

        return sql;
    }
}
