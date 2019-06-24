package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.prefs.Preferences;

public class Main extends Application {

    private static final String WINDOW_POSITION_X = "Window_Position_X";
    private static final String WINDOW_POSITION_Y = "Window_Position_Y";
    private static final String WINDOW_WIDTH = "Window_Width";
    private static final String WINDOW_HEIGHT = "Window_Height";
    private static final String WINDOW_MAXIMIZED = "Window_Maximized";
    private static final double DEFAULT_X = 10;
    private static final double DEFAULT_Y = 10;
    private static final double DEFAULT_WIDTH = 1280;
    private static final double DEFAULT_HEIGHT = 720;
    private static final boolean DEFAULT_MAXIMIZED = false;
    private static final String NODE_NAME = "ViewSwitcher";
    private static final String BUNDLE = "Bundle";

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        Pane root = loader.load();
        Controller controller = loader.getController();
        controller.init();
        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add("sample/style.css");
        primaryStage.setScene(scene);
        primaryStage.show();

        Preferences pref = Preferences.userRoot().node(NODE_NAME);
        double x = pref.getDouble(WINDOW_POSITION_X, DEFAULT_X);
        double y = pref.getDouble(WINDOW_POSITION_Y, DEFAULT_Y);
        double width = pref.getDouble(WINDOW_WIDTH, DEFAULT_WIDTH);
        double height = pref.getDouble(WINDOW_HEIGHT, DEFAULT_HEIGHT);
        boolean maximized = pref.getBoolean(WINDOW_MAXIMIZED, DEFAULT_MAXIMIZED);

        if (!maximized)
        {
            primaryStage.setX(x);
            primaryStage.setY(y);
            primaryStage.setWidth(width);
            primaryStage.setHeight(height);
            primaryStage.setMaximized(false);
        }
        else
        {
            primaryStage.setMaximized(true);
        }

        primaryStage.setOnCloseRequest((final WindowEvent event) -> {
            Preferences preferences = Preferences.userRoot().node(NODE_NAME);
            preferences.putDouble(WINDOW_POSITION_X, primaryStage.getX());
            preferences.putDouble(WINDOW_POSITION_Y, primaryStage.getY());
            preferences.putDouble(WINDOW_WIDTH, primaryStage.getWidth());
            preferences.putDouble(WINDOW_HEIGHT, primaryStage.getHeight());
            preferences.putBoolean(WINDOW_MAXIMIZED, primaryStage.isMaximized());
        });

        primaryStage.setMaximized(true);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
