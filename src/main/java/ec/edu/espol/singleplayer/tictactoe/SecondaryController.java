package ec.edu.espol.singleplayer.tictactoe;

import ec.edu.espol.singleplayertictactoe.constants.GameState;
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

public class SecondaryController {
   private PrimaryController  ventana; 
   private Stage stage ;
   
    @FXML
    private RadioButton Seleccionhumano;
    @FXML
    private ToggleGroup Juegahumano;
    @FXML
    private RadioButton Seleccioncompuitadora;
    @FXML
    private Button BotonJugar;
    @FXML
    private Button BotonRegresar;
    @FXML
    private Label Titulo;
    

    public PrimaryController getPrimaryController() {
        return ventana;
    }
    @FXML
    private void Jugar(ActionEvent event) throws IOException {
       // Guardar quién inicia
    boolean humanStarts = Seleccionhumano.isSelected();
    GameState.setHumanStarts(humanStarts);
    
    // Debug: imprimir las selecciones
    //System.out.println("Símbolo seleccionado: " + GameState.getSelectedSymbol());
    //System.out.println("Inicia humano: " + GameState.doesHumanStart());
    
    // Cargar la ventana del juego
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("game.fxml"));
    Parent root = fxmlLoader.load();
    GameController controller = fxmlLoader.getController();
    Scene scene = new Scene(root, 840, 480);
    Stage newStage = new Stage();  
    newStage.setTitle("TicTacToe game");
    newStage.setScene(scene);
    controller.init(newStage, this);  
    newStage.show();
    stage.close();
    
    }

    @FXML
    private void Regresar(ActionEvent event) {
        ventana.show();
        stage.close();
    }

   public void init(Stage stage , PrimaryController Ventana1) {
        this.ventana = Ventana1;
        this.stage= stage;
        
        
    }
   
}