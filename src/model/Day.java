package model;

import tree_table.CustomTableRow;

import java.time.LocalDate;
import java.util.ArrayList;

public class Day implements CustomTableRow
{
    private LocalDate date;
    private ArrayList<PD_pair> values;
    private Day prev_row;
    private long total;
    private Long total_diff;
    private ModelController modelControllerRef;

    private ArrayList<Trade> applicable_trades;

    Day(LocalDate date, Day prev_row, ModelController modelController)
    {
        applicable_trades = new ArrayList<>();
        modelControllerRef = modelController;
        this.date = date;
        this.prev_row = prev_row;
        values = new ArrayList<>(modelController.getPosition_ids().size() - 1);
        for (int i = 0; i < modelController.getPosition_ids().size() - 1; i++)
        {
            values.add(null);
        }
        for (Integer position_id : modelController.getPosition_ids())
        {
            Position tempPos = modelController.getPositions().get(position_id);
            if (tempPos.getColumn_index() >= 0)
                values.set(tempPos.getColumn_index(), new PD_pair(tempPos, null));
        }

        generate_values();
    }

    public void reload()
    {
        for (int i = 0; i < values.size(); i++)
        {
            PD_pair pd_pair = values.get(i);

            if (prev_row == null)
            {
                if (pd_pair.getPosition().getStart_date().isEqual(date))
                {
                    pd_pair.setValue(pd_pair.getPosition().getStart_value());
                }
            }
            else
            {
                PD_pair prev_pair = prev_row.getValues().get(i);

                if (pd_pair.getPosition().getStart_date().isBefore(date))
                {
                    pd_pair.setValue(prev_pair.getValue());
                    apply_trades(pd_pair);
                }
                else if (pd_pair.getPosition().getStart_date().isEqual(date))
                {
                    pd_pair.setValue(pd_pair.getPosition().getStart_value());
                    apply_trades(pd_pair);
                }
            }
        }

        total = 0;
        for (PD_pair pair : values)
        {
            if (pair.getValue() != null)
                total += pair.getValue();
        }

        if (prev_row != null)
            total_diff = total - prev_row.getTotal();
    }

    void generate_values()
    {
        for (int i = 0; i < values.size(); i++)
        {
            PD_pair pair = values.get(i);

            if (prev_row == null)
            {
                if (pair.getPosition().getStart_date().isEqual(date))
                {
                    pair.setValue(pair.getPosition().getStart_value());
                    find_trades(pair);
                    apply_trades(pair);
                }
            }
            else
            {
                PD_pair prev_pair = prev_row.getValues().get(i);

                if (pair.getPosition().getStart_date().isBefore(date))
                {
                    pair.setValue(prev_pair.getValue());
                    find_trades(pair);
                    apply_trades(pair);
                }
                else if (pair.getPosition().getStart_date().isEqual(date))
                {
                    pair.setValue(pair.getPosition().getStart_value());
                    find_trades(pair);
                    apply_trades(pair);
                }
            }
        }

        total = 0;
        for (PD_pair pair : values)
        {
            if (pair.getValue() != null)
                total += pair.getValue();
        }

        if (prev_row != null)
            total_diff = total - prev_row.getTotal();
    }

    private void find_trades(PD_pair pair)
    {

        if (modelControllerRef.getTrade_stack().get(pair.getPosition()).size() <= 0)
        {
            return;
        }

        while(modelControllerRef.getTrade_stack().get(pair.getPosition()).size() > 0)
        {
            if (modelControllerRef.getTrade_stack().get(pair.getPosition()).peek().getDate().isAfter(date))
            {
                break;
            }

            Trade tempTrade = modelControllerRef.getTrade_stack().get(pair.getPosition()).pop();

            applicable_trades.add(tempTrade);
        }
    }

    private void apply_trades(PD_pair pair)
    {
        for (Trade t : applicable_trades)
        {
            if (t.getPosten_id_to() == pair.getPosition().getId())
            {
                pair.setValue(pair.getValue() + t.getValue());
            }
            if (t.getPosten_id_from() == pair.getPosition().getId())
            {
                pair.setValue(pair.getValue() - t.getValue());
            }
        }
    }

    public boolean did_value_change(int column)
    {
        if (prev_row == null)
        {
            return true;
        }

        if (values.get(column).getValue() != prev_row.getValues().get(column).getValue())
        {
            return true;
        }

        return false;
    }

    public long get_row_difference(int column)
    {
        if (prev_row == null)
        {
            return values.get(column).getValue();
        }
        else
        {
            return prev_row.getValues().get(column).getValue() - values.get(column).getValue();
        }
    }

    void onEditButtonPress()
    {

    }

    public ArrayList<PD_pair> getValues()
    {
        return values;
    }

    public void setValues(ArrayList<PD_pair> values)
    {
        this.values = values;
    }

    @Override
    public LocalDate getDate()
    {
        return date;
    }

     @Override
    public Long getTotal()
    {
        return total;
    }

    @Override
    public Long getTotal_Diff()
    {
        return total_diff;
    }

    public void addTrade(Trade trade)
    {
        applicable_trades.add(trade);
    }

    public ArrayList<Trade> getTrades()
    {
        return applicable_trades;
    }
}
