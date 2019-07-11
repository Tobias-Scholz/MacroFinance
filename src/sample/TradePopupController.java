package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.Day;
import model.ModelController;
import model.Trade;
import model.Utils;

import java.util.prefs.Preferences;

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
    TableColumn<TradeRow, Button> submit_column;
    @FXML
    TableColumn<TradeRow, Button> delete_column;

    private final String ID_COLUMN_WIDTH = "id_column_width";
    private final String TO_POSITION_COLUMN_WIDTH = "to_position_column_width";
    private final String FROM_POSITION_COLUMN_WIDTH = "from_position_column_width";
    private final String VALUE_COLUMN_WIDTH = "value_column_width";
    private final String DATE_COLUMN_WIDTH = "date_column_width";
    private final String CATEGORY_COLUMN_WIDTH = "category_column_width";
    private final String DESCRIPTION_COLUMN_WIDTH = "description_column_width";
    private final String SUBMIT_COLUMN_WIDTH = "submit_column_width";
    private final String DELETE_COLUMN_WIDTH = "delete_column_width";

    private final double DEFAULT_ID_COLUMN_WIDTH = 35;
    private final double DEFAULT_TO_POSITION_COLUMN_WIDTH = 120;
    private final double DEFAULT_FROM_POSITION_COLUMN_WIDTH = 120;
    private final double DEFAULT_VALUE_COLUMN_WIDTH = 85;
    private final double DEFAULT_DATE_COLUMN_WIDTH = 110;
    private final double DEFAULT_CATEGORY_COLUMN_WIDTH = 100;
    private final double DEFAULT_DESCRIPTION__COLUMN_WIDTH = 180;
    private final double DEFAULT_SUBMIT_COLUMN_WIDTH = 70;
    private final double DEFAULT_DELETE_COLUMN_WIDTH = 40;

    void init(Day day, ModelController modelController, Controller controller, Stage stage)
    {
        id_column.setCellValueFactory(tradeIntegerCellDataFeatures -> new SimpleObjectProperty<>(tradeIntegerCellDataFeatures.getValue().getTrade().getId()));
        to_position_column.setCellValueFactory(tradeStringCellDataFeatures -> new SimpleObjectProperty<>(tradeStringCellDataFeatures.getValue().getTo_id_combobox()));
        from_position_column.setCellValueFactory(tradeStringCellDataFeatures -> new SimpleObjectProperty<>(tradeStringCellDataFeatures.getValue().getFrom_id_combobox()));
        value_column.setCellValueFactory(tradeDoubleCellDataFeatures -> new SimpleObjectProperty<>(tradeDoubleCellDataFeatures.getValue().getValue_textfield()));
        description_column.setCellValueFactory(tradeStringCellDataFeatures -> new SimpleObjectProperty<>(tradeStringCellDataFeatures.getValue().getDescription_textfield()));
        category_column.setCellValueFactory(tradeStringCellDataFeatures -> new SimpleObjectProperty<>(tradeStringCellDataFeatures.getValue().getCategory_id_combobox()));
        date_column.setCellValueFactory(tradeStringCellDataFeatures -> new SimpleObjectProperty<>(tradeStringCellDataFeatures.getValue().getDatePicker()));
        submit_column.setCellValueFactory(tradeRowButtonCellDataFeatures -> new SimpleObjectProperty<>(tradeRowButtonCellDataFeatures.getValue().getSubmitButton()));
        delete_column.setCellValueFactory(tradeRowButtonCellDataFeatures -> new SimpleObjectProperty<>(tradeRowButtonCellDataFeatures.getValue().getDeleteButton()));

        id_column.setPrefWidth(DEFAULT_ID_COLUMN_WIDTH);
        to_position_column.setPrefWidth(DEFAULT_TO_POSITION_COLUMN_WIDTH);
        from_position_column.setPrefWidth(DEFAULT_FROM_POSITION_COLUMN_WIDTH);
        value_column.setPrefWidth(DEFAULT_VALUE_COLUMN_WIDTH);
        description_column.setPrefWidth(DEFAULT_DESCRIPTION__COLUMN_WIDTH);
        category_column.setPrefWidth(DEFAULT_CATEGORY_COLUMN_WIDTH);
        date_column.setPrefWidth(DEFAULT_DATE_COLUMN_WIDTH);
        submit_column.setPrefWidth(DEFAULT_SUBMIT_COLUMN_WIDTH);
        delete_column.setPrefWidth(DEFAULT_DELETE_COLUMN_WIDTH);

        double window_width = 0;
        for (TableColumn tableColumn : tableView.getColumns())
        {
            tableColumn.setSortable(false);
            window_width += tableColumn.getWidth() + 2;
        }
        stage.setWidth(window_width);

        double old_description_width = description_column.getWidth();
        double old_window_width = stage.getWidth();
        stage.widthProperty().addListener(observable -> {
            description_column.setPrefWidth(old_description_width - (old_window_width - stage.getWidth()));
        });

        ObservableList<TradeRow> items = FXCollections.observableArrayList();
        for (Trade trade : day.getTrades())
        {
            items.add(new TradeRow(trade, modelController, controller));
        }

        tableView.setItems(items);
        tableView.setSelectionModel(null);
    }

    void on_close()
    {
        Preferences.userRoot().node("MacroFinance").putDouble(ID_COLUMN_WIDTH, id_column.getWidth());

    }
}
