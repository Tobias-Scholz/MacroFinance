package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Database;

import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Position
{
    private int id;
    private String name;
    private LocalDate start_date;
    private long start_value;
    private int column_index;
    private Type type;

    public enum Type
    {
        external, Giro, Depot, Cash, Demand, P2P
    }

    public Position(int id, String name, Date start_date, long start_value, int type, int column_index)
    {
        this.id = id;
        this.name = name;
        this.start_value = start_value;
        this.start_date = start_date.toLocalDate();
        this.column_index = column_index;
        switch (type)
        {
            case 0: this.type = Type.external; break;
            case 1: this.type = Type.Giro; break;
            case 2: this.type = Type.Depot; break;
            case 3: this.type = Type.Cash; break;
            case 4: this.type = Type.Demand; break;
            case 5: this.type = Type.P2P; break;
        }
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public LocalDate getStart_date()
    {
        return start_date;
    }

    public void setStart_date(LocalDate start_date)
    {
        this.start_date = start_date;
    }

    public long getStart_value()
    {
        return start_value;
    }

    public void setStart_value(long start_value)
    {
        this.start_value = start_value;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public int getColumn_index()
    {
        return column_index;
    }

    public void setColumn_index(int column_index)
    {
        this.column_index = column_index;
    }
}

