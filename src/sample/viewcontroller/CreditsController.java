package sample.viewcontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class CreditsController {

    @FXML
    private VBox leftBox;
    @FXML
    private VBox rightBox;
    @FXML
    private BorderPane root;

    @FXML
    void initialize() {
//        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
//        leftBox.setPrefWidth((screenSize.getWidth() - 70)/2);
//        rightBox.setPrefWidth((screenSize.getWidth() - 70)/2);
    }

    public void backToIntro(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("intro.fxml"));
            Parent root = loader.load();
            Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
            Scene newScene = new Scene(root, screenSize.getWidth(), screenSize.getHeight());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setMaximized(true);
            stage.setScene(newScene);
            stage.setTitle("Intro");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
