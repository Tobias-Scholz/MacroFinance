package tree_table;

import model.PD_pair;
import model.Position;
import model.Trade;
import sample.Database;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public interface CustomTableRow
{
    LocalDate getDate();
    Long getTotal();
    Long getTotal_Diff();
    ArrayList<PD_pair> getValues();
}
