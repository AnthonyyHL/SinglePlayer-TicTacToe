package ec.edu.espol.singleplayer.tictactoe;

import ec.edu.espol.singleplayertictactoe.constants.GameState;
import ec.edu.espol.singleplayertictactoe.constants.GameTurns;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

public class PrimaryController {
    private Stage stage;
    
    @FXML
    private Label Title;
    @FXML
    private Label seleccion;
    @FXML
    private RadioButton BotonX;
    @FXML
    private ToggleGroup JuegaconX;
    @FXML
    private RadioButton Boton0;
    @FXML
    private Button Botonconfirmar;

    

    @FXML
    private void Confirmar(ActionEvent event) throws IOException {
       // Obtener el botón seleccionado
        RadioButton selectedRadioButton = (RadioButton) JuegaconX.getSelectedToggle();
        
        // Extraer solo X u O del texto del botón
        char symbol = selectedRadioButton == BotonX ? GameTurns.X_TURNS : GameTurns.O_TURNS;
        
        // Guardar la selección en GameState
        GameState.setSelectedSymbol(symbol);
        
        // Debug: imprimir la selección
        //System.out.println("Jugador eligió: " + symbol);
        
        // Cargar la siguiente ventana
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("secondary.fxml"));
        Parent root = fxmlLoader.load();
        SecondaryController controller = fxmlLoader.getController();
        Scene scene = new Scene(root, 640, 480);
        Stage newStage = new Stage();
        stage.setTitle("TicTacToe game");
        newStage.setScene(scene);
        controller.init(newStage, this);
        newStage.show();
        stage.close();
        
    }

    public void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    public void show() {
        stage.show();
    }
   
}