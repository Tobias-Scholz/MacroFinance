package sample;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import model.ModelController;
import model.Trade;

public class TradeRow
{
    private Trade real_trade;
    private Trade pre_save_trade;
    private SimpleBooleanProperty submit_allowed;

    private ComboBox<Integer> category_id_combobox;

    public TradeRow(Trade real_trade, ModelController modelController)
    {
        this.real_trade = real_trade;
        pre_save_trade = new Trade(real_trade);
        category_id_combobox = new ComboBox<>();
        category_id_combobox.setItems(modelController.getCategory_ids());
        category_id_combobox.setConverter(new StringConverter<Integer>()
        {
            @Override
            public String toString(Integer integer)
            {
                return modelController.getCategories().get(integer);
            }

            @Override
            public Integer fromString(String s)
            {
                return null;
            }
        });
        category_id_combobox.getSelectionModel().select(real_trade.getCategory_id());
    }

    public Trade getReal_trade()
    {
        return real_trade;
    }

    public Trade getPre_save_trade()
    {
        return pre_save_trade;
    }

    public ComboBox<Integer> getCategory_id_combobox()
    {
        return category_id_combobox;
    }
}
