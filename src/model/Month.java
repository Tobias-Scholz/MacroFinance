package model;

import tree_table.CustomTableRow;

import java.time.LocalDate;
import java.util.ArrayList;

public class Month implements CustomTableRow
{
    private Day last_day_of_month;
    private Month lastMonth;
    private LocalDate date;
    private ArrayList<Day> children;

    public Month(LocalDate date, Month lastMonth)
    {
        this.date = date;
        this.lastMonth = lastMonth;
        children = new ArrayList<>();
    }

    public void reload()
    {

    }

    public Day getLast_day_of_month()
    {
        return last_day_of_month;
    }

    public void setLast_day_of_month(Day last_day_of_month)
    {
        this.last_day_of_month = last_day_of_month;
    }

    @Override
    public LocalDate getDate()
    {
        return date;
    }

    @Override
    public Long getTotal()
    {
        return last_day_of_month.getTotal();
    }

    @Override
    public Long getTotal_Diff()
    {
        if (lastMonth == null) return null;
        else return last_day_of_month.getTotal() - lastMonth.getTotal();
    }

    @Override
    public ArrayList<PD_pair> getValues()
    {
        return last_day_of_month.getValues();
    }

    public ArrayList<Day> getChildren()
    {
        return children;
    }

    public void setChildren(ArrayList<Day> children)
    {
        this.children = children;
    }
}
