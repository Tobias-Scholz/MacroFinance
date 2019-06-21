package tree_table;

import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.paint.Color;
import model.Day;
import model.Month;
import model.Utils;
import model.Year;

import java.time.LocalDate;

class DeltaTreeColumn extends TreeTableColumn<CustomTableRow, CustomTableRow>
{
    DeltaTreeColumn()
    {
        super("Delta △");

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
                    setAlignment(Pos.CENTER_RIGHT);

                    if (item != null)
                    {
                        Long total_diff = item.getTotal_Diff();
                        if (total_diff != null)
                        {
                            if (total_diff > 0)
                            {
                                draw_positve_number(this, total_diff);
                            }
                            else if (total_diff == 0)
                            {
                                draw_neutral_number(this);
                            }
                            else
                            {
                                draw_negative_number(this, total_diff);
                            }
                        }
                        else
                        {
                            draw_empty_cell(this);
                        }
                    }
                    else
                    {
                        draw_empty_cell(this);
                    }
                }
            }
        });
        this.setCellValueFactory(customTableRowStringCellDataFeatures -> new SimpleObjectProperty<>(customTableRowStringCellDataFeatures.getValue().getValue()));
    }

    public void draw_positve_number(TreeTableCell cell, long val)
    {
        cell.setText(Utils.format_money_to_string(val));
        cell.setTextFill(Color.GREEN);
    }

    public void draw_negative_number(TreeTableCell cell, long val)
    {
        cell.setText(Utils.format_money_to_string(val));
        cell.setTextFill(Color.RED);
    }

    public void draw_neutral_number(TreeTableCell cell)
    {
        cell.setText("0.00");
        cell.setTextFill(Color.LIGHTGRAY);
    }

    public void draw_empty_cell(TreeTableCell cell)
    {
        cell.setText("");
        cell.setStyle(null);
    }
}
