package custom_chart;

import java.time.LocalDate;

public class Chart_data
{
    private LocalDate date;
    private double value;

    public Chart_data(LocalDate date, double value)
    {
        this.date = date;
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

    public double getValue()
    {
        return value;
    }

    public void setValue(double value)
    {
        this.value = value;
    }
}
