package model;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Database;

import java.awt.image.DataBuffer;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ModelController
{
    private ArrayList<Day> all_days;
    private ArrayList<Month> all_months;
    private ArrayList<Year> all_years;

    private HashMap<Integer, String> categories;
    private ObservableList<Integer> category_ids;
    private HashMap<Integer, Trade> trade_container;

    private Stack<Integer> trade_stack;

    private Map<Integer, Position> positions = new HashMap<>();
    private ObservableList<Integer> position_ids = FXCollections.observableArrayList();
    private LocalDate first_date;

    public ModelController()
    {
        all_days = new ArrayList<>();
        all_months = new ArrayList<>();
        all_years = new ArrayList<>();

        load_trades();
        load_categories();
        loadPositions();
        load_trade_stack(first_date);

        reload();
    }

    void reload()
    {
        LocalDate temp_date = first_date;

        Day current_day = null;
        Month current_month = null;
        Year current_year = null;

        while(temp_date.isBefore(LocalDate.now()) || temp_date.isEqual(LocalDate.now()))
        {
            if (current_year == null)
            {
                current_year = new Year(temp_date, null);
                all_years.add(current_year);
            }
            if (current_month == null)
            {
                current_month = new Month(temp_date, null);
                all_months.add(current_month);
                current_year.getChildren().add(current_month);
            }

            if (current_year.getDate().getYear() != temp_date.getYear())
            {
                if (current_day != null)
                    current_year.setLast_day_of_year(current_day);

                current_year = new Year(temp_date, current_year);
                all_years.add(current_year);
            }

            if (current_month.getDate().getMonth() != temp_date.getMonth())
            {
                if (current_day != null)
                    current_month.setLast_day_of_month(current_day);

                current_month = new Month(temp_date, current_month);
                all_months.add(current_month);
                current_year.getChildren().add(current_month);
            }

            Day temp_day = new Day(temp_date, current_day, this);
            all_days.add(temp_day);
            current_day = temp_day;
            current_month.getChildren().add(current_day);
            temp_date = temp_date.plusDays(1);
        }

        current_year.setLast_day_of_year(current_day);
        current_month.setLast_day_of_month(current_day);
    }

    private void load_trade_stack(LocalDate start_date)
    {
        trade_stack = new Stack<>();

        try
        {
            ResultSet resultSet = Database.query("SELECT * FROM Trade WHERE date(date)>=date(\"" + start_date.toString() + "\") ORDER BY date(date) DESC");
            while (resultSet.next())
            {
                trade_stack.push(resultSet.getInt("id"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void load_trades()
    {
        trade_container = new HashMap<>();

        ResultSet resultSet = Database.query("SELECT * FROM Trade");

        try
        {
            while (resultSet.next())
            {
                Trade t = new Trade(
                        resultSet.getInt("id"),
                        resultSet.getInt("from_id"),
                        resultSet.getInt("to_id"),
                        resultSet.getString("description"),
                        resultSet.getLong("value"),
                        resultSet.getDate("date"),
                        resultSet.getInt("category_id")
                );
                trade_container.put(t.getId(), t);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void load_categories()
    {
        categories = new HashMap<>();
        category_ids = FXCollections.observableArrayList();

        ResultSet resultSet = Database.query("SELECT * FROM Trade_Category");

        try
        {
            while (resultSet.next())
            {
                categories.put(resultSet.getInt("id"), resultSet.getString("name"));
                category_ids.add(resultSet.getInt("id"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void insert_trade_into_db(int from_id, int to_id, String description, double value, LocalDate date, int category_id)
    {
        Connection con = Database.getCon();

        try
        {
            PreparedStatement prep = con.prepareStatement("INSERT INTO Trade (from_id, to_id, description, value, date, category_id) VALUES (?,?,?,?,?,?)");
            prep.setInt(1, from_id);
            prep.setInt(2, to_id);
            prep.setString(3, description);
            prep.setLong(4, (long) (value * 100));
            prep.setString(5, java.sql.Date.valueOf(date).toString() + " 00:00:00.000");
            prep.setInt(6, category_id);
            prep.execute();

            ResultSet rs = Database.query("SELECT * FROM Trade WHERE id = LAST_INSERT_ROWID()");

            while (rs.next())
            {
                Trade trade = new Trade(rs.getInt("id"), from_id, to_id, description, (long) (value * 100), date, category_id);
                trade_container.put(trade.getId(), trade);

                for (Day d : all_days)
                {
                    if (d.getDate().isEqual(trade.getDate()))
                    {
                        d.addTrade(trade);
                    }
                }

                reload_after(date);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void reload_after(LocalDate date)
    {
        int start_year_index = -1;
        int start_month_index = -1;
        int start_day_index = -1;

        for (int i = 0; i < all_years.size(); i++)
        {
            if (all_years.get(i).getDate().getYear() == date.getYear())
            {
                start_year_index = i;
            }
        }

        for (int i = 0; i < all_months.size(); i++)
        {
            if (all_months.get(i).getDate().getMonthValue() == date.getMonthValue() && all_months.get(i).getDate().getYear() == date.getYear())
            {
                start_month_index = i;
            }
        }

        for (int i = 0; i < all_days.size(); i++)
        {
            if (all_days.get(i).getDate().isEqual(date))
            {
                start_day_index = i;
            }
        }

        for (int i = start_day_index; i < all_days.size(); i++)
        {
            all_days.get(i).reload();
        }
        for (int i = start_month_index; i < all_months.size(); i++)
        {
            all_months.get(i).reload();
        }
        for (int i = start_year_index; i < all_years.size(); i++)
        {
            all_years.get(i).reload();
        }
    }

    public void loadPositions()
    {
        ResultSet resultSet = Database.query("SELECT * FROM Position");
        positions.clear();
        position_ids.clear();

        try
        {
            while (resultSet.next())
            {


                Position p = new Position(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDate("start_date"),
                        resultSet.getInt("start_value"),
                        resultSet.getInt("type"),
                        resultSet.getInt("column_index")
                );
                positions.put(p.getId(), p);
                position_ids.add(p.getId());

                if (p.getType() != Position.Type.external)
                {
                    if (first_date == null)
                    {
                        first_date = p.getStart_date();
                    }
                    else if (p.getStart_date().isBefore(first_date))
                    {
                        first_date = p.getStart_date();
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void update_trade(Trade trade, LocalDate old_date)
    {
        Connection con = Database.getCon();

        try
        {
            PreparedStatement prep = con.prepareStatement("UPDATE Trade SET from_id = ?, to_id = ?, description = ?, value = ?, date = ?, category_id = ? WHERE id = ?;");
            prep.setInt(1, trade.getPosten_id_from());
            prep.setInt(2, trade.getPosten_id_to());
            prep.setString(3, trade.getDescription());
            prep.setLong(4, trade.getValue());
            prep.setString(5, java.sql.Date.valueOf(trade.getDate()).toString() + " 00:00:00.000");
            prep.setInt(6, trade.getCategory_id());
            prep.setInt(7, trade.getId());
            prep.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        load_trade_stack(old_date.isBefore(trade.getDate()) ? old_date : trade.getDate());
        reload_after(old_date.isBefore(trade.getDate()) ? old_date : trade.getDate());
    }

    public ArrayList<Day> getAll_days()
    {
        return all_days;
    }

    public ArrayList<Month> getAll_months()
    {
        return all_months;
    }

    public ArrayList<Year> getAll_years()
    {
        return all_years;
    }

    public HashMap<Integer, String> getCategories()
    {
        return categories;
    }

    public ObservableList<Integer> getCategory_ids()
    {
        return category_ids;
    }

    public HashMap<Integer, Trade> getTrade_container()
    {
        return trade_container;
    }

    public Map<Integer, Position> getPositions()
    {
        return positions;
    }

    public ObservableList<Integer> getPosition_ids()
    {
        return position_ids;
    }

    public LocalDate getFirst_date()
    {
        return first_date;
    }

    public Stack<Integer> getTrade_stack()
    {
        return trade_stack;
    }
}