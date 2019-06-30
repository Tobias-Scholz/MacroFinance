package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import model.*;

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

        Long value = -1l;
        Integer to_position_id = toPositionChoice.getSelectionModel().getSelectedItem();
        Integer from_position_id = fromPositionChoice.getSelectionModel().getSelectedItem();
        String description = commentField.getText();
        Integer category_id = categoryChoiceBox.getSelectionModel().getSelectedItem();
        LocalDate date = datePicker.getValue();

        TradeError error = Trade.verify_trade(
                fromPositionChoice.getSelectionModel().getSelectedItem(),
                toPositionChoice.getSelectionModel().getSelectedItem(),
                valueField.getText(),
                commentField.getText(),
                datePicker.getValue(),
                categoryChoiceBox.getSelectionModel().getSelectedItem()
        );

        if (error.getErrors()[3])
        {
            valueField.setStyle("-fx-border-color: #FF0000");
        }
        else
        {
            value = Utils.convert_string_to_money(valueField.getText());
        }

        if (error.getErrors()[0])
        {
            toPositionChoice.setStyle("-fx-border-color: #FF0000");
            fromPositionChoice.setStyle("-fx-border-color: #FF0000");
        }

        if (error.getErrors()[5])
        {
            categoryChoiceBox.setStyle("-fx-border-color: #FF0000");
        }

        if (!error.any_error())
        {
            superControllerRef.insert_new_trade(from_position_id, to_position_id, description, value, date, category_id);
        }
        else
        {
            add_text_ln(errorText, error.getError_text());
        }
    }

    private void add_text_ln(Text text, String toAppend)
    {
        text.setText(text.getText() + "\n" + toAppend);
    }
}
