package ec.edu.espol.singleplayer.tictactoe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
       FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("primary.fxml"));
       Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 640, 480);
        stage.setTitle("TicTacToe game");
        stage.setScene(scene);
        PrimaryController controller = fxmlLoader.getController();
        controller.setStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
       
        launch();
    }

}







