package tree_table;

import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.paint.Color;
import model.Day;
import model.Utils;

public class TotalTableColumn extends TreeTableColumn<CustomTableRow, CustomTableRow>
{
    TotalTableColumn()
    {
        super("Total");

        this.setCellFactory(customTableRowLocalDateTreeTableColumn -> new TreeTableCell<>()
        {
            @Override
            public void updateItem(CustomTableRow item, boolean empty)
            {
                if (empty)
                {
                    setText(null);
                    setGraphic(null);
                } else
                {
                    setAlignment(Pos.CENTER_RIGHT);

                    if (item instanceof Day)
                    {
                        Day day = (Day) item;
                        setText(Utils.format_money_to_string(day.getTotal()));

                        if (Math.abs(item.getTotal_Diff()) > 0)
                        {
                            setTextFill(Color.BLACK);
                        }
                        else
                        {
                            setTextFill(Color.DARKGRAY);
                        }
                    }
                    else
                    {
                        setText(Utils.format_money_to_string(item.getTotal()));
                    }
                }
            }
        });
        this.setCellValueFactory(customTableRowStringCellDataFeatures -> new SimpleObjectProperty<>(customTableRowStringCellDataFeatures.getValue().getValue()));
    }
}
