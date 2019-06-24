package sample;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.Day;
import model.ModelController;
import model.Trade;
import model.Utils;

public class TradePopupController
{
    @FXML
    TableView<TradeRow> tableView;
    @FXML
    TableColumn<TradeRow, Integer> id_column;
    @FXML
    TableColumn<TradeRow, ComboBox<Integer>> to_position_column;
    @FXML
    TableColumn<TradeRow, ComboBox<Integer>> from_position_column;
    @FXML
    TableColumn<TradeRow, TextField> value_column;
    @FXML
    TableColumn<TradeRow, TextField> description_column;
    @FXML
    TableColumn<TradeRow, ComboBox<Integer>> category_column;
    @FXML
    TableColumn<TradeRow, DatePicker> date_column;
    @FXML
    TableColumn<TradeRow, Boolean> delete_edit_column;

    void init(Day day, ModelController modelController)
    {
        id_column.setCellValueFactory(tradeIntegerCellDataFeatures -> new SimpleObjectProperty<>(tradeIntegerCellDataFeatures.getValue().getTrade().getId()));
        to_position_column.setCellValueFactory(tradeStringCellDataFeatures -> new SimpleObjectProperty<>(tradeStringCellDataFeatures.getValue().getTo_id_combobox()));
        from_position_column.setCellValueFactory(tradeStringCellDataFeatures -> new SimpleObjectProperty<>(tradeStringCellDataFeatures.getValue().getFrom_id_combobox()));
        value_column.setCellValueFactory(tradeDoubleCellDataFeatures -> new SimpleObjectProperty<>(tradeDoubleCellDataFeatures.getValue().getValue_textfield()));
        description_column.setCellValueFactory(tradeStringCellDataFeatures -> new SimpleObjectProperty<>(tradeStringCellDataFeatures.getValue().getDescription_textfield()));
        category_column.setCellValueFactory(tradeStringCellDataFeatures -> new SimpleObjectProperty<>(tradeStringCellDataFeatures.getValue().getCategory_id_combobox()));
        date_column.setCellValueFactory(tradeStringCellDataFeatures -> new SimpleObjectProperty<>(tradeStringCellDataFeatures.getValue().getDatePicker()));
        // delete_edit_column.setCellValueFactory(tradeStringCellDataFeatures -> tradeStringCellDataFeatures.getValue().getReal_trade().any_field_changedProperty());

        id_column.setStyle("-fx-alignment: CENTER");


        delete_edit_column.setCellFactory(tradeBooleanTableColumn -> new TableCell<>()
        {
            Button button = new Button("Submit");

            @Override
            public void updateItem(Boolean item, boolean empty)
            {
                if (item != null)
                {
                    setGraphic(button);
                    if (item)
                    {
                        button.setOpacity(1);
                    }
                    else
                    {
                        button.setOpacity(0);
                    }
                }
            }
        });

        ObservableList<TradeRow> items = FXCollections.observableArrayList();
        for (Trade trade : day.getTrades())
        {
            items.add(new TradeRow(trade, modelController));
        }

        tableView.setItems(items);
        tableView.setSelectionModel(null);
    }
}
