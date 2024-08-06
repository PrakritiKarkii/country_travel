package org.example.countrytravel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load your FXML file
        URL fxmlLocation = getClass().getResource("/org/example/countrytravel/view.fxml");
        if (fxmlLocation == null) {
            throw new IllegalStateException("FXML file not found");
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        VBox root = loader.load();
        Image icon = new Image(getClass().getResourceAsStream("/org/example/countrytravel/logo.png"));
        primaryStage.getIcons().add(icon);
        // Apply CSS
        Scene scene = new Scene(root);
        URL cssLocation = getClass().getResource("/org/example/countrytravel/style.css");
        if (cssLocation == null) {
            throw new IllegalStateException("CSS file not found");
        }
        scene.getStylesheets().add(cssLocation.toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Country Travel");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
