package tree_table;

import javafx.scene.control.*;
import model.*;
import sample.Controller;
import sample.Database;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class CustomTreeTable
{
    private TreeTableView<CustomTableRow> treeTableView;
    private ModelController modelController;
    private Controller superController;

    public CustomTreeTable(TreeTableView<CustomTableRow> treeTableView, ModelController modelController, Controller superController)
    {
        this.superController = superController;
        this.treeTableView = treeTableView;
        this.modelController = modelController;
    }

    public void init()
    {
        ArrayList<TreeTableColumn> columns = new ArrayList<>();

        DateTreeColumn dateTreeColumn = new DateTreeColumn();
        treeTableView.getColumns().addAll(dateTreeColumn);
        dateTreeColumn.setComparator(Comparator.comparing(CustomTableRow::getDate));

        dateTreeColumn.setMinWidth(130);
        TotalTableColumn totalTableColumn = new TotalTableColumn();

        for (Integer pos_id : modelController.getPosition_ids())
        {
            if (modelController.getPositions().get(pos_id).getType() != Position.Type.external)
            {
                PositionTreeColumn column = new PositionTreeColumn(modelController.getPositions().get(pos_id), modelController.getPositions().get(pos_id).getName(), superController);
                columns.add(column);
                treeTableView.getColumns().add(column);
            }
        }

        treeTableView.getColumns().add(totalTableColumn);

        DeltaTreeColumn deltaTreeColumn = new DeltaTreeColumn();
        treeTableView.getColumns().add(deltaTreeColumn);

        TreeItem<CustomTableRow> root = new TreeItem<>(new Year(LocalDate.now(), null));

        for (Year y : modelController.getAll_years())
        {
            TreeItem<CustomTableRow> yearItem = new TreeItem<>(y);
            root.getChildren().add(yearItem);
            for (Month m : y.getChildren())
            {
                TreeItem<CustomTableRow> monthItem = new TreeItem<>(m);
                yearItem.getChildren().add(monthItem);
                for (Day d : m.getChildren())
                {
                    monthItem.getChildren().add(new TreeItem<>(d));
                }
            }
        }

        treeTableView.setSelectionModel(null);
        treeTableView.setShowRoot(false);
        treeTableView.setRoot(root);
        treeTableView.refresh();
    }
}
