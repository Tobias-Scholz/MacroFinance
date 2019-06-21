package custom_chart;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.Chart;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.time.LocalDate;
import java.util.ArrayList;

public class CChart
{
    private Pane pane;
    private Canvas canvas;
    private GraphicsContext gc;

    private ArrayList<Chart_data> data;

    private LocalDate start_date;
    private LocalDate end_date;
    private double lowest_value = 0.0;
    private double highest_value = 0.0;

    private final double chart_left_offset = 80.0;
    private final double chart_lower_offset = 120.0;
    private final double chart_upper_offset = 80.0;
    private final double chart_right_offset = 80.0;

    private int chart_left_label_amount_goal = 10;
    private double dotted_line_opacity = 0.4;

    private boolean data_set = false;
    private static double[] steps;

    private double step;
    private int step_amount;
    private double interval_y;
    private double interval_x;
    private double total_y;

    private Color daily_total_color = Color.LIMEGREEN;
    private Color monthly_total_color = Color.CORNFLOWERBLUE;

    private ArrayList<Node> labels;

    static
    {
        steps = new double[30];
        steps[0] = 1.0;
        int counter = 0;

        for (int i = 1; i < 30; i++)
        {
            switch (counter)
            {
                case 0: steps[i] = steps[i - 1] * 2.5;  counter++;      break;
                case 1: steps[i] = steps[i - 1] * 2;    counter++;      break;
                case 2: steps[i] = steps[i - 1] * 2;    counter -= 2;   break;
            }
        }
    }

    public CChart(Pane pane)
    {
        this.pane = pane;
        canvas = new Canvas(pane.getHeight(), pane.getWidth());
        pane.getChildren().add(canvas);

        pane.heightProperty().addListener(observable ->
        {
            canvas.setHeight(pane.getHeight());
            redraw_chart();
        });
        pane.widthProperty().addListener(observable ->
        {
            canvas.setWidth(pane.getWidth());
            redraw_chart();
        });
        gc = canvas.getGraphicsContext2D();

        data = new ArrayList<>();
        labels = new ArrayList<>();

        generate_legend();
    }

    private void draw_data_and_xAxis()
    {
        if (data_set)
        {
            total_y = (highest_value - lowest_value) / 100.0;
            step = find_step(total_y / (double) chart_left_label_amount_goal);
            step_amount = (int) (total_y / step) + 2;
            interval_y = ((canvas.getHeight() - chart_lower_offset) - chart_upper_offset) / (step * (step_amount - 1)) * 0.95;

            int size = data.size();

            double last_x = 0;
            double last_y = data.get(0).getValue() / 100.0 * interval_y;

            double last_monthly_x = last_x;
            double last_monthly_y = last_y;

            for (int i = 1; i < size; i++)
            {
                double x = (((canvas.getWidth() - chart_right_offset - chart_left_offset) / size)) * i;
                double y = data.get(i).getValue() / 100.0 * interval_y;

                if (data.get(i).getDate().getDayOfYear() == 1)
                {
                    gc.setStroke(Color.DARKGRAY);
                    gc.setLineWidth(1.0);
                    gc.setLineDashOffset(1.0);
                    gc.strokeLine(x + chart_left_offset, canvas.getHeight() - chart_lower_offset, x + chart_left_offset, chart_upper_offset);

                    gc.setLineWidth(2.0);
                    gc.setStroke(monthly_total_color);
                    gc.strokeLine(Math.floor(x + chart_left_offset), canvas.getHeight() - chart_lower_offset - y, Math.floor(last_monthly_x + chart_left_offset), canvas.getHeight() - chart_lower_offset - last_monthly_y);
                    last_monthly_x = x;
                    last_monthly_y = y;

                    Text textNode = new Text(Integer.toString(data.get(i).getDate().getYear()));
                    labels.add(textNode);
                    textNode.setFont(Font.font ("Verdana", 18));
                    textNode.setBoundsType(TextBoundsType.VISUAL);
                    textNode.relocate(x + chart_left_offset, canvas.getHeight() - chart_lower_offset);
                    textNode.applyCss();
                    textNode.getTransforms().add(new Rotate(-45));
                    textNode.getTransforms().add(new Translate(-textNode.getBoundsInLocal().getWidth(), 5));
                    // textNode.relocate(textNode.getLayoutX() - textNode.getBoundsInLocal().getWidth(), textNode.getLayoutY() + textNode.getBoundsInLocal().getHeight());
                    pane.getChildren().add(textNode);
                }
                else if (data.get(i).getDate().getDayOfMonth() == 1)
                {
                    gc.setStroke(Color.DARKGRAY);
                    gc.save();
                    gc.setLineWidth(1.0);
                    gc.setLineDashes(3.0);
                    gc.setGlobalAlpha(dotted_line_opacity);
                    gc.strokeLine(Math.floor(x + chart_left_offset) + 0.5, canvas.getHeight() - chart_lower_offset, Math.floor(x + chart_left_offset) + 0.5, chart_upper_offset);
                    gc.restore();

                    gc.setLineWidth(2.0);
                    gc.setStroke(monthly_total_color);
                    gc.strokeLine(Math.floor(x + chart_left_offset), canvas.getHeight() - chart_lower_offset - y, Math.floor(last_monthly_x + chart_left_offset), canvas.getHeight() - chart_lower_offset - last_monthly_y);
                    last_monthly_x = x;
                    last_monthly_y = y;

                    Text textNode = new Text(data.get(i).getDate().getMonth().toString());
                    labels.add(textNode);
                    textNode.setFont(Font.font ("Verdana", 10));
                    textNode.setBoundsType(TextBoundsType.VISUAL);
                    textNode.relocate(x + chart_left_offset, canvas.getHeight() - chart_lower_offset);
                    textNode.applyCss();
                    textNode.getTransforms().add(new Rotate(-45));
                    textNode.getTransforms().add(new Translate(-textNode.getBoundsInLocal().getWidth(), 5));
                    // textNode.relocate(textNode.getLayoutX() - textNode.getBoundsInLocal().getWidth(), textNode.getLayoutY() + textNode.getBoundsInLocal().getHeight());
                    pane.getChildren().add(textNode);
                }

                gc.setLineWidth(2.0);
                gc.setStroke(daily_total_color);
                gc.strokeLine(Math.floor(x + chart_left_offset), canvas.getHeight() - chart_lower_offset - y, Math.floor(last_x + chart_left_offset), canvas.getHeight() - chart_lower_offset - last_y);

                last_x = x;
                last_y = y;
            }
        }
    }

    private void draw_border_lines()
    {
        gc.beginPath();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.moveTo(chart_left_offset, chart_upper_offset);
        gc.lineTo(chart_left_offset, canvas.getHeight() - chart_lower_offset);
        gc.lineTo(canvas.getWidth() - chart_right_offset, canvas.getHeight() - chart_lower_offset);
        gc.stroke();
        gc.closePath();
        gc.setLineCap(StrokeLineCap.BUTT);
    }

    private void draw_yAxis()
    {
        for (int i = 0; i < step_amount; i++)
        {
            Text textNode = new Text(Integer.toString((int)(i * step)));
            textNode.applyCss();
            textNode.relocate(chart_left_offset - textNode.getBoundsInLocal().getWidth() - 5, canvas.getHeight() - chart_lower_offset - (interval_y * i * step) - textNode.getBoundsInLocal().getHeight() / 2 - 2);
            pane.getChildren().add(textNode);
            labels.add(textNode);

            gc.save();

            gc.setLineWidth(1.0);
            gc.setLineDashes(3.0);
            gc.setGlobalAlpha(dotted_line_opacity);
            gc.setStroke(Color.DARKGRAY);
            gc.strokeLine(chart_left_offset, (int) (canvas.getHeight() - chart_lower_offset - (interval_y * i * step)) + 0.5, canvas.getWidth() - chart_right_offset, (int) (canvas.getHeight() - chart_lower_offset - (interval_y * i * step)) + 0.5);

            gc.restore();
        }
    }

    public void redraw_chart()
    {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Node t : labels)
        {
            pane.getChildren().remove(t);
        }

        draw_border_lines();
        draw_data_and_xAxis();
        draw_yAxis();
    }

    public void generate_legend()
    {
        HBox legendPane = new HBox(4);
        legendPane.setSpacing(0);
        legendPane.setPadding(new Insets(12));
        pane.getChildren().add(legendPane);
        Label l1 = new Label("Daily_Total");
        Circle c1 = new Circle(7.5);
        c1.setFill(Color.WHITE);
        c1.setStroke(daily_total_color);
        c1.setStrokeWidth(3);
        HBox.setMargin(c1, new Insets(0, 20, 0, 5));

        Label l2 = new Label("Monthly_Total");
        Circle c2 = new Circle(7.5);
        c2.setFill(Color.WHITE);
        c2.setStroke(monthly_total_color);
        c2.setStrokeWidth(3);
        HBox.setMargin(c2, new Insets(0, 0, 0, 5));


        legendPane.getChildren().add(l1);
        legendPane.getChildren().add(c1);
        legendPane.getChildren().add(l2);
        legendPane.getChildren().add(c2);
        legendPane.setStyle("-fx-padding: 10");
        legendPane.setStyle("-fx-border-color: #000000");
        legendPane.setEffect(new InnerShadow(BlurType.THREE_PASS_BOX, new Color(0.5, 0.5, 0.5, 1.0), 7, 0, 0, 0));
        legendPane.setStyle("-fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5 5 5 5");
        legendPane.applyCss();
        legendPane.relocate(pane.getWidth() / 2.0, 0.0);
        legendPane.requestFocus();
        legendPane.layout();
        legendPane.requestLayout();

        pane.heightProperty().addListener(observable -> update_legend_pane(legendPane));
        pane.widthProperty().addListener(observable -> update_legend_pane(legendPane));
        legendPane.heightProperty().addListener(observable -> update_legend_pane(legendPane));
        legendPane.widthProperty().addListener(observable -> update_legend_pane(legendPane));
    }

    private void update_legend_pane(HBox legendPane)
    {
        legendPane.relocate(pane.getWidth() / 2.0 - legendPane.getWidth() / 2.0, pane.getHeight() - 60.0);
    }

    private double find_step(double val)
    {
        int index = 0;

        while (val > steps[index])
        {
            index++;
        }
        return steps[index];
    }

    public void add_data(Chart_data data)
    {
        end_date = data.getDate();
        if (data.getValue() > highest_value)
        {
            highest_value = data.getValue();
        }
        this.data.add(data);
        data_set = true;
    }
}
