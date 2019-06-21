package tree_table;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseEvent;
import model.Day;
import model.Month;
import model.Year;

public class DateTreeColumn extends TreeTableColumn<CustomTableRow, CustomTableRow>
{
    DateTreeColumn()
    {
        super("Date");

        this.setCellFactory(customTableRowLocalDateTreeTableColumn -> new TreeTableCell<>()
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
                    if (item instanceof Day)
                    {
                        setStyle("");
                        setText(((Day) item).getDate().toString());
                    }
                    else if (item instanceof Month)
                    {
                        setStyle("-fx-font-size: 14px");
                        setText(item.getDate().getMonth().toString());
                    }
                    else if (item instanceof Year)
                    {
                        setStyle("-fx-font-size: 18px");
                        setText(Integer.toString(item.getDate().getYear()));
                    }
                }



                setEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> getTreeTableRow().getTreeItem().setExpanded(!getTreeTableRow().getTreeItem().isExpanded()));
            }
        });
        this.setCellValueFactory(customTableRowStringCellDataFeatures -> new SimpleObjectProperty<>(customTableRowStringCellDataFeatures.getValue().getValue()));
    }
}
