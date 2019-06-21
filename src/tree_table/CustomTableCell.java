package tree_table;

import javafx.scene.control.TreeTableCell;

public class CustomTableCell extends TreeTableCell<CustomTableRow, Double>
{
    public CustomTableCell() {}

    @Override
    protected void updateItem(final Double item, boolean empty)
    {
        if (item != null) {
            setText(item.toString());
        }
    }
}
