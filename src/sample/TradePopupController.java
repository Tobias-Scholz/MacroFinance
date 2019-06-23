package sample;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    TableColumn<Trade, Integer> to_position_column;
    @FXML
    TableColumn<Trade, Integer> from_position_column;
    @FXML
    TableColumn<Trade, String> value_column;
    @FXML
    TableColumn<Trade, String> description_column;
    @FXML
    TableColumn<Trade, Integer> category_column;
    @FXML
    TableColumn<Trade, String> date_column;
    @FXML
    TableColumn<Trade, Boolean> delete_edit_column;

    public void init(Day day, ModelController modelController)
    {
        id_column.setCellValueFactory(tradeIntegerCellDataFeatures -> new SimpleObjectProperty<>(tradeIntegerCellDataFeatures.getValue().getId()));
        to_position_column.setCellValueFactory(tradeStringCellDataFeatures -> new SimpleObjectProperty<>(tradeStringCellDataFeatures.getValue().getPosten_id_to()));
        from_position_column.setCellValueFactory(tradeStringCellDataFeatures -> new SimpleObjectProperty<>(tradeStringCellDataFeatures.getValue().getPosten_id_from()));
        value_column.setCellValueFactory(tradeDoubleCellDataFeatures -> new SimpleObjectProperty<>(Utils.format_money_to_string(tradeDoubleCellDataFeatures.getValue().getValue())));
        description_column.setCellValueFactory(tradeStringCellDataFeatures -> new SimpleObjectProperty<>(tradeStringCellDataFeatures.getValue().getDescription()));
        category_column.setCellValueFactory(tradeStringCellDataFeatures -> new SimpleObjectProperty<>(tradeStringCellDataFeatures.getValue().getCategory_id()));
        date_column.setCellValueFactory(tradeStringCellDataFeatures -> new SimpleObjectProperty<>(tradeStringCellDataFeatures.getValue().getDate().toString()));
        delete_edit_column.setCellValueFactory(tradeStringCellDataFeatures -> tradeStringCellDataFeatures.getValue().any_field_changedProperty());

        to_position_column.setCellFactory(tradeStringTableColumn -> new ComboBoxTableCell<Integer>(modelController.getPosition_ids())
        {
            @Override
            public void updateItem(Integer item, boolean empty)
            {
                if (item != null)
                {
                    setText("");
                    setGraphic(comboBox);
                    comboBox.getSelectionModel().select(item);

                    comboBox.setOnAction(actionEvent -> {
                        getTableView().getItems().get(getIndex()).getCached_trade().setPosten_id_to(comboBox.getSelectionModel().getSelectedItem());
                    });
                }
            }

            @Override
            public void init()
            {
                comboBox.setConverter(new StringConverter<>()
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
                });
            }
        });

        from_position_column.setCellFactory(tradeStringTableColumn -> new ComboBoxTableCell<Integer>(modelController.getPosition_ids())
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
                comboBox.setConverter(new StringConverter<>()
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
                });
            }
        });

        category_column.setCellFactory(tradeStringTableColumn -> new ComboBoxTableCell<>(modelController.getCategory_ids())
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
        tableView.setSelectionModel(null);
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

    ComboBoxTableCell(ObservableList<T> items)
    {
       load_values(items);
       init();
    }

    public abstract void updateItem(T item, boolean empty);

    private void load_values(ObservableList<T> items)
    {
        comboBox.setItems(items);
    }

    public abstract void init();
}
