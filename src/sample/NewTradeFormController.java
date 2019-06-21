package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import model.ModelController;
import model.Position;
import model.Trade;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class NewTradeFormController
{
    @FXML
    private ChoiceBox<Integer> fromPositionChoice;
    @FXML
    private ChoiceBox<Integer> toPositionChoice;
    @FXML
    private ToggleButton arrowButton;
    @FXML
    private TextField valueField;
    @FXML
    private TextField commentField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<Integer> categoryChoiceBox;
    @FXML
    private Button submitButton;
    @FXML
    private Text errorText;

    private ModelController modelController;
    private Controller superControllerRef;

    @FXML
    public void init(ModelController modelController, Controller superControllerRef)
    {
        this.superControllerRef = superControllerRef;
        this.modelController = modelController;

        arrowButton.setStyle("-fx-background-image: url('/ArrowDown.png');");
        arrowButton.setOnAction(actionEvent -> {

            int from_id = fromPositionChoice.getSelectionModel().getSelectedIndex();
            int to_id = toPositionChoice.getSelectionModel().getSelectedIndex();

            fromPositionChoice.getSelectionModel().select(to_id);
            toPositionChoice.getSelectionModel().select(from_id);
        });

        StringConverter<Integer> positionChoiceBoxStringConverter = new StringConverter<Integer>()
        {
            @Override
            public String toString(Integer integer)
            {
                return modelController.getPositions().get(integer).getName();
            }

            @Override
            public Integer fromString(String s)
            {
                return null;
            }
        };

        StringConverter<Integer> categoryChoiceBoxStringConverter = new StringConverter<Integer>()
        {
            @Override
            public String toString(Integer integer)
            {
                return modelController.getCategories().get(integer);
            }

            @Override
            public Integer fromString(String s)
            {
                return null;
            }
        };

        fromPositionChoice.setConverter(positionChoiceBoxStringConverter);
        toPositionChoice.setConverter(positionChoiceBoxStringConverter);
        fromPositionChoice.setItems(modelController.getPosition_ids());
        toPositionChoice.setItems(modelController.getPosition_ids());
        fromPositionChoice.getSelectionModel().select(1);
        toPositionChoice.getSelectionModel().select(0);
        errorText.setText("");
        submitButton.setOnAction(actionEvent -> submit());

        categoryChoiceBox.setItems(modelController.getCategory_ids());
        categoryChoiceBox.setConverter(categoryChoiceBoxStringConverter);

        datePicker.setValue(LocalDate.now());

        errorText.setWrappingWidth(150.0);
        errorText.setFill(Color.RED );
    }

    private void submit()
    {
        fromPositionChoice.setStyle(null);
        valueField.setStyle(null);
        toPositionChoice.setStyle(null);
        fromPositionChoice.setStyle(null);
        categoryChoiceBox.setStyle(null);
        errorText.setText("");

        boolean error = false;
        double value = 0.0;
        int to_position_id;
        int from_position_id;
        String description;
        int category_id;
        LocalDate date = datePicker.getValue();

        if (valueField.getText().length() == 0)
        {
            add_text_ln(errorText, "Value-field can not be empty.");
            valueField.setStyle("-fx-border-color: #FF0000");
            error = true;
        }
        else
        {
            try
            {
                value = Double.valueOf(valueField.getText());
            }
            catch (Exception e)
            {
                add_text_ln(errorText, "Value-field must contain a number.");
                valueField.setStyle("-fx-border-color: #FF0000");
                error = true;
            }
        }

        to_position_id = toPositionChoice.getSelectionModel().getSelectedItem();
        from_position_id = fromPositionChoice.getSelectionModel().getSelectedItem();

        if (to_position_id == from_position_id)
        {
            add_text_ln(errorText, "Positions can not be the same value.");
            toPositionChoice.setStyle("-fx-border-color: #FF0000");
            fromPositionChoice.setStyle("-fx-border-color: #FF0000");
            error = true;
        }

        description = commentField.getText();

        category_id = -1;
        if (categoryChoiceBox.getSelectionModel().getSelectedItem() == null)
        {
            error = true;
            add_text_ln(errorText, "You must select a category.");
            categoryChoiceBox.setStyle("-fx-border-color: #FF0000");
        }
        else
            category_id = categoryChoiceBox.getSelectionModel().getSelectedItem();

        if (!error)
        {
            superControllerRef.insert_new_trade(from_position_id, to_position_id, description, value, date, category_id);
        }
    }

    private void add_text_ln(Text text, String toAppend)
    {
        text.setText(text.getText() + "\n" + toAppend);
    }
}
