package model;

import tree_table.CustomTableRow;

import java.time.LocalDate;
import java.util.ArrayList;

public class Year implements CustomTableRow
{
    private LocalDate date;
    private Year last_year;
    private Day last_day_of_year;
    private ArrayList<Month> children;

    public Year(LocalDate date, Year last_year)
    {
        this.date = date;
        this.last_year = last_year;
        children = new ArrayList<>();
    }

    public void reload()
    {

    }

    public void setLast_day_of_year(Day last_day_of_year)
    {
        this.last_day_of_year = last_day_of_year;
    }

    public Day getLast_day_of_year()
    {
        return last_day_of_year;
    }

    @Override
    public LocalDate getDate()
    {
        return date;
    }

    @Override
    public Long getTotal()
    {
        return last_day_of_year.getTotal();
    }

    @Override
    public Long getTotal_Diff()
    {
        if (last_year != null) return last_day_of_year.getTotal() - last_year.getTotal();
        else return null;
    }

    @Override
    public ArrayList<PD_pair> getValues()
    {
        return last_day_of_year.getValues();
    }

    public ArrayList<Month> getChildren()
    {
        return children;
    }
}
