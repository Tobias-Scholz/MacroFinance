package model;

import tree_table.CustomTableRow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Stack;

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

        reload();
    }

    void reload()
    {
        applicable_trades = new ArrayList<>();

        Stack<Integer> trade_stack = modelControllerRef.getTrade_stack();

        try
        {
            while (modelControllerRef.getTrade_container().get(trade_stack.peek()).getDate().isEqual(date))
            {
                applicable_trades.add(modelControllerRef.getTrade_container().get(trade_stack.pop()));
            }
        }
        catch (Exception e)
        {

        }

        for (int i = 0; i < values.size(); i++)
        {
            PD_pair pair = values.get(i);

            if (prev_row == null)
            {
                if (pair.getPosition().getStart_date().isEqual(date))
                {
                    pair.setValue(pair.getPosition().getStart_value());
                }
            }
            else
            {
                PD_pair prev_pair = prev_row.getValues().get(i);

                if (pair.getPosition().getStart_date().isBefore(date))
                {
                    pair.setValue(prev_pair.getValue());
                }
                else if (pair.getPosition().getStart_date().isEqual(date))
                {
                    pair.setValue(pair.getPosition().getStart_value());
                }
            }
        }

        for (Trade t : applicable_trades)
        {
            Position to_position = modelControllerRef.getPositions().get(t.getPosten_id_to());
            Position from_position = modelControllerRef.getPositions().get(t.getPosten_id_from());

            if (to_position.getId() != 0)
            {
                values.get(to_position.getColumn_index()).setValue(values.get(to_position.getColumn_index()).getValue() + t.getValue());
            }
            if (from_position.getId() != 0)
            {
                values.get(from_position.getColumn_index()).setValue(values.get(from_position.getColumn_index()).getValue() - t.getValue());
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

    void onEditButtonPress()
    {

    }

    public ArrayList<PD_pair> getValues()
    {
        return values;
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
