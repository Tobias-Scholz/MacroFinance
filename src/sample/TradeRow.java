package sample;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import model.ModelController;
import model.Trade;

import java.util.Date;

public class TradeRow
{
    private Trade trade;
    private SimpleBooleanProperty submit_allowed;

    private ComboBox<Integer> category_id_combobox;
    private ComboBox<Integer> from_id_combobox;
    private ComboBox<Integer> to_id_combobox;
    private TextField value_textfield;
    private TextField description_textfield;
    private DatePicker datePicker;
    private Button submitButton;
    private Button deleteButton;

    TradeRow(Trade real_trade, ModelController modelController)
    {
        this.trade = real_trade;
        category_id_combobox = new ComboBox<>();
        category_id_combobox.setItems(modelController.getCategory_ids());
        category_id_combobox.setConverter(new StringConverter<>()
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

        from_id_combobox = new ComboBox<>();
        from_id_combobox.setItems(modelController.getPosition_ids());
        StringConverter<Integer> positionStringConverter = new StringConverter<Integer>()
        {
            @Override
            public String toString(Integer integer)
            {
                return modelController.getPositions().get(integer).getName();
            }

            @Override
            public Integer fromString(String s)
            {
                return null;
            }
        };
        from_id_combobox.setConverter(positionStringConverter);
        from_id_combobox.getSelectionModel().select(real_trade.getPosten_id_from());

        to_id_combobox = new ComboBox<>();
        to_id_combobox.setItems(modelController.getPosition_ids());
        to_id_combobox.setConverter(positionStringConverter);
        to_id_combobox.getSelectionModel().select(real_trade.getPosten_id_to());

        value_textfield = new TextField();
        value_textfield.setText(model.Utils.format_money_to_string(real_trade.getValue()));

        description_textfield = new TextField();
        description_textfield.setText(real_trade.getDescription());

        datePicker = new DatePicker();
        datePicker.setValue(trade.getDate());

        submitButton = new Button("Submit");
        deleteButton = new Button("X");

        submitButton.setOnAction(actionEvent ->
        {

        });
    }

    public Trade getTrade()
    {
        return trade;
    }

    public ComboBox<Integer> getCategory_id_combobox()
    {
        return category_id_combobox;
    }

    public boolean isSubmit_allowed()
    {
        return submit_allowed.get();
    }

    public SimpleBooleanProperty submit_allowedProperty()
    {
        return submit_allowed;
    }

    public ComboBox<Integer> getFrom_id_combobox()
    {
        return from_id_combobox;
    }

    public ComboBox<Integer> getTo_id_combobox()
    {
        return to_id_combobox;
    }

    public TextField getValue_textfield()
    {
        return value_textfield;
    }

    public TextField getDescription_textfield()
    {
        return description_textfield;
    }

    public DatePicker getDatePicker()
    {
        return datePicker;
    }

    public Button getSubmitButton()
    {
        return submitButton;
    }

    public Button getDeleteButton()
    {
        return deleteButton;
    }
}
