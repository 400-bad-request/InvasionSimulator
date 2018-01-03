package sample.viewcontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class IntroController {

    @FXML
    public void initialize() {}

    public void submit(ActionEvent actionEvent) {

        try {
            // Load new view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("config.fxml"));
            Parent root = loader.load();
            // Get screen size of monitor
            Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
            // Creating new scene
            Scene newScene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
            // Acquire stage
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            // Embedding new stage and configuring it's parameters
            stage.setMaximized(true);
            stage.setScene(newScene);
            stage.setTitle("Configuration");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
