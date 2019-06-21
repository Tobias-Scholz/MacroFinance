package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;

public class Trade
{
    private int id;
    private int posten_id_from;
    private int posten_id_to;
    private String description;
    private long value;
    private LocalDate date;
    private int category_id;

    public Trade(int id, int posten_id_from, int posten_id_to, String description, long value, Date date, int category_id)
    {
        this.id = id;
        this.posten_id_from = posten_id_from;
        this.posten_id_to = posten_id_to;
        this.description = description;
        this.value = value;
        this.date = date.toLocalDate();
        this.category_id = category_id;
    }

    public Trade(int id, int posten_id_from, int posten_id_to, String description, long value, LocalDate date, int category_id)
    {
        this.id = id;
        this.posten_id_from = posten_id_from;
        this.posten_id_to = posten_id_to;
        this.description = description;
        this.value = value;
        this.date = date;
        this.category_id = category_id;
    }

    private static void create_random_trade()
    {

    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getPosten_id_from()
    {
        return posten_id_from;
    }

    public void setPosten_id_from(int posten_id_from)
    {
        this.posten_id_from = posten_id_from;
    }

    public int getPosten_id_to()
    {
        return posten_id_to;
    }

    public void setPosten_id_to(int posten_id_to)
    {
        this.posten_id_to = posten_id_to;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public long getValue()
    {
        return value;
    }

    public void setValue(long        value)
    {
        this.value = value;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public int getCategory_id()
    {
        return category_id;
    }

    public void setCategory_id(int category_id)
    {
        this.category_id = category_id;
    }
}
