package sample;

import javafx.beans.property.SimpleBooleanProperty;
import model.Trade;

public class TradeRow
{
    private Trade real_trade;
    private Trade pre_save_trade;
    private SimpleBooleanProperty submit_allowed;

    public TradeRow(Trade real_trade)
    {
        this.real_trade = real_trade;
        pre_save_trade = new Trade(real_trade);
    }

    public Trade getReal_trade()
    {
        return real_trade;
    }

    public Trade getPre_save_trade()
    {
        return pre_save_trade;
    }
}
