package sample;

import custom_chart.CChart;
import custom_chart.Chart_data;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.ModelController;
import model.Trade;
import tree_table.CustomTableRow;
import tree_table.CustomTreeTable;
import model.Day;

import java.time.LocalDate;

public class Controller
{
    @FXML
    BorderPane parentPane;
    @FXML
    private TreeTableView<CustomTableRow> treeTableView;
    @FXML
    private Pane chartPane;
    @FXML
    private NewTradeFormController newTradeFormController;

    private CustomTreeTable treeTable;
    private Database db;
    private CChart chart;

    ModelController modelController;

    void init()
    {
        Database.getConnection();
        modelController = new ModelController();
        treeTable = new CustomTreeTable(treeTableView, modelController, this);
        treeTable.init();
        initChart();

        newTradeFormController.init(modelController, this);
    }

    void initChart()
    {
        chart = new CChart(chartPane);

        for (Day day : modelController.getAll_days())
        {
            chart.add_data(new Chart_data(day.getDate(), day.getTotal()));
        }
    }

    public void insert_new_trade(int from_id, int to_id, String description, double value, LocalDate date, int category_id)
    {
        modelController.insert_trade_into_db(from_id, to_id, description, value, date, category_id);
        treeTableView.refresh();
    }

    public void open_trade_popup(Day day)
    {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TradeInfo.fxml"));
        stage.setTitle(day.getDate().toString());
        try
        {
            Pane root = loader.load();
            TradePopupController controller = loader.getController();
            controller.init(day, modelController, this, stage);
            Scene scene = new Scene(root);
            stage.setOnCloseRequest(windowEvent -> controller.on_close());
            scene.getStylesheets().add("sample/style.css");
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void update_trade(Trade trade, LocalDate old_date)
    {
        modelController.update_trade(trade, old_date);
        treeTableView.refresh();
        chart.redraw_chart();
    }

    public void save_preferences()
    {
        treeTable.save_preferences();
    }
}