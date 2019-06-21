package tree_table;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.*;
import sample.Controller;

public class PositionTreeColumn extends TreeTableColumn<CustomTableRow, CustomTableRow>
{
    private Position ref_position;
    private Controller superControllerRef;

    PositionTreeColumn(Position ref_position, String name, Controller controller)
    {
        super(name);
        superControllerRef = controller;
        this.ref_position = ref_position;

        this.setCellFactory(customTableRowStringTreeTableColumn -> new TreeTableCell<>()
        {
            @Override
            public void updateItem(CustomTableRow item, boolean empty)
            {
                if (empty)
                {
                    setText(null);
                    setGraphic(null);
                }
                else
                {
                    setAlignment(Pos.CENTER_RIGHT);

                    if (item instanceof Day)
                    {
                        Day day = (Day) item;
                        if(day.getValues().get(ref_position.getColumn_index()).getValue() != null)
                        {
                            setText(Utils.format_money_to_string(day.getValues().get(ref_position.getColumn_index()).getValue()));

                            Color cellColor;

                            if (day.did_value_change(ref_position.getColumn_index()))
                            {
                                cellColor = Color.BLACK;
                            }
                            else
                            {
                                cellColor = Color.DARKGRAY;
                            }
                            setTextFill(cellColor);

                            setOnMouseEntered(mouseEvent ->
                            {
                                setStyle("-fx-border-color: #B0B0B0; -fx-border-radius: 2px; -fx-background-color: linear-gradient(#FFFFFF, #F0F0F0)");
                                setTextFill(Color.BLACK);
                            });

                            setOnMousePressed(mouseEvent -> setStyle("-fx-border-color: #B0B0B0; -fx-border-radius: 2px; -fx-background-color: linear-gradient(#F0F0F0, #FFFFFF)"));

                            setOnMouseClicked(mouseEvent ->
                            {
                                setStyle("-fx-border-color: #B0B0B0; -fx-border-radius: 2px; -fx-background-color: linear-gradient(#FFFFFF, #F0F0F0)");
                                superControllerRef.open_trade_popup(day);
                            });

                            setOnMouseExited(mouseEvent ->
                            {
                                setStyle(null);
                                setTextFill(cellColor);
                            });
                        }
                        else
                            setText("");
                    }
                    else if (item instanceof Month)
                    {
                        setTextFill(Color.BLACK);

                        Month month = (Month) item;

                        if (month.getLast_day_of_month().getValues().get(ref_position.getColumn_index()).getValue() != null)
                            setText(Utils.format_money_to_string(month.getLast_day_of_month().getValues().get(ref_position.getColumn_index()).getValue()));

                    }
                    else
                    {
                        setTextFill(Color.BLACK);

                        Year year = (Year) item;

                        if (year.getLast_day_of_year().getValues().get(ref_position.getColumn_index()).getValue() != null)
                        {
                            setText(Utils.format_money_to_string(year.getLast_day_of_year().getValues().get(ref_position.getColumn_index()).getValue()));
                        }
                    }
                }
            }
        });
        this.setCellValueFactory(customTableRowStringCellDataFeatures -> new SimpleObjectProperty<>(customTableRowStringCellDataFeatures.getValue().getValue()));
    }
}
