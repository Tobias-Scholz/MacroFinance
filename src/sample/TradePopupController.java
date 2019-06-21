package sample;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.Day;
import model.ModelController;
import model.Trade;
import model.Utils;

public class TradePopupController
{
    @FXML
    TableView<Trade> tableView;
    @FXML
    TableColumn<Trade, Integer> id_column;
    @FXML
    TableColumn<Trade, String> to_position_column;
    @FXML
    TableColumn<Trade, String> from_position_column;
    @FXML
    TableColumn<Trade, String> value_column;
    @FXML
    TableColumn<Trade, String> description_column;
    @FXML
    TableColumn<Trade, Integer> category_column;
    @FXML
    TableColumn<Trade, String> date_column;

    public void init(Day day, ModelController modelController)
    {
        id_column.setCellValueFactory(tradeIntegerCellDataFeatures -> new SimpleObjectProperty<>(tradeIntegerCellDataFeatures.getValue().getId()));
        to_position_column.setCellValueFactory(tradeStringCellDataFeatures -> new SimpleObjectProperty<>(modelController.getPositions().get(tradeStringCellDataFeatures.getValue().getPosten_id_to()).getName()));
        from_position_column.setCellValueFactory(tradeStringCellDataFeatures -> new SimpleObjectProperty<>(modelController.getPositions().get(tradeStringCellDataFeatures.getValue().getPosten_id_from()).getName()));
        value_column.setCellValueFactory(tradeDoubleCellDataFeatures -> new SimpleObjectProperty<>(Utils.format_money_to_string(tradeDoubleCellDataFeatures.getValue().getValue())));
        description_column.setCellValueFactory(tradeStringCellDataFeatures -> new SimpleObjectProperty<>(tradeStringCellDataFeatures.getValue().getDescription()));
        category_column.setCellValueFactory(tradeStringCellDataFeatures -> new SimpleObjectProperty<>(tradeStringCellDataFeatures.getValue().getCategory_id()));
        date_column.setCellValueFactory(tradeStringCellDataFeatures -> new SimpleObjectProperty<>(tradeStringCellDataFeatures.getValue().getDate().toString()));

        id_column.setCellFactory(tradeIntegerTableColumn -> new TableCell<>()
        {

        });

        to_position_column.setCellFactory(tradeStringTableColumn -> new TableCell<>()
        {
            ComboBox<Integer> comboBox = new ComboBox<>();


            @Override
            public void updateItem(String item, boolean empty)
            {
                setText("");

                if (item != null)
                {
                    comboBox.setItems(modelController.getPosition_ids());
                }
            }
        });

        category_column.setCellFactory(new Callback<TableColumn<Trade, Integer>, TableCell<Trade, Integer>>()
        {
            @Override
            public ComboBoxTableCell<Integer> call(TableColumn<Trade, Integer> tradeIntegerTableColumn)
            {
                return null;
            }
        });

        category_column.setCellFactory(tradeStringTableColumn -> new ComboBoxTableCell<Integer>(modelController.getCategory_ids())
        {
            @Override
            public void updateItem(Integer item, boolean empty)
            {
                if (item != null)
                {
                    setText("");
                    setGraphic(comboBox);
                    comboBox.getSelectionModel().select(item);
                }
            }

            @Override
            public void init()
            {
                comboBox.setConverter(new StringConverter<Integer>()
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
            }
        });

        ObservableList<Trade> items = FXCollections.observableArrayList();
        items.addAll(day.getTrades());

        tableView.setItems(items);
    }
}

class TextFieldIntegerTableCell extends TableCell<Trade, Integer>
{
    TextField textField = new TextField();

    @Override
    public void updateItem(Integer item, boolean empty)
    {
        setText("");

        if (item != null)
        {
            textField.setText(Integer.toString(item));
            setGraphic(textField);
        }
    }
}

abstract class ComboBoxTableCell<T> extends TableCell<Trade, T>
{
    ComboBox<T> comboBox = new ComboBox<>();

    public ComboBoxTableCell(ObservableList<T> items)
    {
       load_values(items);
       init();
    }

    public abstract void updateItem(T item, boolean empty);

    void load_values(ObservableList<T> items)
    {
        comboBox.setItems(items);
    }

    public abstract void init();
}