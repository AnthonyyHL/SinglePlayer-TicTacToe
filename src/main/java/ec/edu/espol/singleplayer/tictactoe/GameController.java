/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ec.edu.espol.singleplayer.tictactoe;

import ec.edu.espol.singleplayertictactoe.constants.GameState;
import ec.edu.espol.singleplayertictactoe.constants.GameTurns;
import ec.edu.espol.singleplayertictactoe.model.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

/**
 * FXML Controller class
 *
 * @author samir
 */
public class GameController {
    private Stage stage;
    private SecondaryController ventanaAnterior;
    private Game game;
    private char currentPlayer;
    private boolean gameEnded = false;
    private boolean isPaused = false;
    @FXML
    private Button RegresarBt;
    @FXML
    private GridPane tablero;
    @FXML
    private GridPane tableroAnterior;
    @FXML
    private GridPane tableroMejor;
    @FXML
    private Label labelestado;
    @FXML
    private Button Botonjugardenuevo;
    @FXML
    private Button BtPausaresume;
    @FXML
    private Label labelTurno;
    
     
    
      
    

    @FXML
    private void Regresarventana2(ActionEvent event) throws IOException {
        // Cargar la ventana anterior (secondary.fxml)
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("secondary.fxml"));
        Parent root = fxmlLoader.load();
        SecondaryController controller = fxmlLoader.getController();
        Scene scene = new Scene(root, 640, 480);
        Stage newStage = new Stage();
        newStage.setTitle("TicTacToe game");
        newStage.setScene(scene);
        controller.init(newStage, ventanaAnterior.getPrimaryController());
        newStage.show();
        stage.close();
    }
    public void init(Stage stage, SecondaryController ventanaAnterior) {
        this.stage = stage;
        this.ventanaAnterior = ventanaAnterior;
        this.game = new Game();
        this.currentPlayer = GameState.doesHumanStart() ? 
            GameState.getSelectedSymbol() : 
            (GameState.getSelectedSymbol() == GameTurns.X_TURNS) ? GameTurns.O_TURNS : GameTurns.X_TURNS;

        // Inicializar estado de botones
        BtPausaresume.setText("Pausar");
        isPaused = false;

        inicializarTablero();
        actualizarTurno();
        if (!GameState.doesHumanStart()) {
            realizarMovimientoRandom();
        }
    }

    private void inicializarTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                StackPane cell = createCell(i, j);
                StackPane cell1 = createCellApart(i, j);
                StackPane cell2 = createCellApart(i, j);
                tablero.add(cell, j, i); // Ahora usa el nombre correcto tablero
                tableroAnterior.add(cell1,j,i);
                tableroMejor.add(cell2,j,i);
            }
        }
        
    }
    
    private StackPane createCellApart(int row, int col) {
        StackPane cell = new StackPane();
        cell.setPrefSize(100, 100);
        cell.setStyle("-fx-background-color: #03186b; -fx-border-color: #94dff8;");
        
        Text text = new Text();
        text.setFont(Font.font("System", 25));
        text.setFill(javafx.scene.paint.Color.valueOf("#94dff8"));
        
        cell.getChildren().add(text);
        return cell;
    }
    
    private StackPane createCell(int row, int col) {
        StackPane cell = new StackPane();
        cell.setPrefSize(100, 100);
        cell.setStyle("-fx-background-color: #03186b; -fx-border-color: #94dff8;");
        
        Text text = new Text();
        text.setFont(Font.font("System", 40));
        text.setFill(javafx.scene.paint.Color.valueOf("#94dff8"));
        
        cell.getChildren().add(text);
        
        cell.setOnMouseClicked(event -> handleCellClick(event, row, col, text));
        
        return cell;
    }
    
    private void handleCellClick(MouseEvent event, int row, int col, Text text) {
        // Verificar si es un movimiento válido y si es el turno del jugador
    if (!isPaused && text.getText().isEmpty() && !gameEnded && 
        currentPlayer == GameState.getSelectedSymbol()) {
        realizarMovimiento(row, col, text);
        
        if (!gameEnded) {
            realizarMovimientoIA();
        }
    }
    }
    
    private void realizarMovimiento(int row, int col, Text text) {
        text.setText(String.valueOf(currentPlayer));
        if (currentPlayer == GameTurns.X_TURNS) {
            text.setFill(javafx.scene.paint.Color.valueOf("#FF867F")); // Rojo tomate para 'X'
        } else {
            text.setFill(javafx.scene.paint.Color.valueOf("#A4FF80")); // Verde lima para 'O'
        }
        game.getBoard()[row][col] = currentPlayer;
        //Muestra los movimientos anteriores del humano
        if (currentPlayer == GameState.getSelectedSymbol()) {
            StackPane cellAnterior = (StackPane) getNodeFromGridPane(tableroAnterior, col, row);
            if (cellAnterior != null ) {
                Text textAnterior = (Text) cellAnterior.getChildren().get(0);
                textAnterior.setText(String.valueOf(currentPlayer));
                textAnterior.setFill(text.getFill());
            }
        }
        verificarEstadoJuego();
        
        if (!gameEnded) {
            // Cambiar al siguiente jugador
            currentPlayer = (currentPlayer == GameTurns.X_TURNS) ? GameTurns.O_TURNS : GameTurns.X_TURNS;
            actualizarTurno();
        }
    }
    
    private void realizarMovimientoRandom(){
        Random random = new Random();
        int randomI = random.nextInt(3);
        int randomJ = random.nextInt(3);
        StackPane cellRandom = (StackPane) getNodeFromGridPane(tablero, randomJ, randomI);
                if (cellRandom != null) {
                    Text text = (Text) cellRandom.getChildren().get(0);
                    realizarMovimiento(randomI,randomJ, text);
                }
    }
    
    private void realizarMovimientoIA() {
        try {
            int[] mejorMovimiento = game.findBestMove();
            if (mejorMovimiento != null) {
                StackPane cell = (StackPane) getNodeFromGridPane(tablero, mejorMovimiento[1], mejorMovimiento[0]);
                if (cell != null) {
                    Text text = (Text) cell.getChildren().get(0);
                    realizarMovimiento(mejorMovimiento[0], mejorMovimiento[1], text);
                }
            }
            verificarEstadoJuego();
            
            // Calcular mejor jugada posible del jugador humano
            int[] mejorMovimientoHumano = game.findBestMove();
            limpiarTablero(tableroMejor);
            if (mejorMovimientoHumano != null && !gameEnded) {
                StackPane cellMejor = (StackPane) getNodeFromGridPane(tableroMejor, mejorMovimientoHumano[1], mejorMovimientoHumano[0]);
                if (cellMejor != null) {
                    Text textMejor = (Text) cellMejor.getChildren().get(0);
                    textMejor.setText(String.valueOf(GameState.getSelectedSymbol()));
                    textMejor.setFill(javafx.scene.paint.Color.valueOf("#FFD700")); // Color dorado para destacar
                }
            }
        } catch (Exception e) {
            System.err.println("Error en movimiento IA: " + e.getMessage());
            // Puedes mostrar un mensaje al usuario si lo deseas
            labelestado.setText("Error en el movimiento de la IA");
        }
    }
    
    private void limpiarTablero(GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof StackPane) {
                StackPane cell = (StackPane) node;
                Text text = (Text) cell.getChildren().get(0);
                text.setText("");
            }
        }
    }
    
    private void actualizarTurno() {
        if (currentPlayer == GameState.getSelectedSymbol()) {
            labelTurno.setText("Turno de la persona: " + currentPlayer);
        } else {
            labelTurno.setText("Turno de la IA: " + currentPlayer);
        }
    }

    
    private void verificarEstadoJuego() {
        String estado = game.getGameStatus();
        if (!estado.equals("En curso")) {
            gameEnded = true;
            labelestado.setText(estado);
        }
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
    for (Node node : gridPane.getChildren()) {
        Integer columnIndex = GridPane.getColumnIndex(node);
        Integer rowIndex = GridPane.getRowIndex(node);
        
        if (columnIndex != null && rowIndex != null && 
            columnIndex == col && rowIndex == row) {
            return node;
        }
    }
    return null;
}

    @FXML
    private void handlePlayAgain(ActionEvent event) {
        // Reiniciar el juego
    this.game = new Game();
    this.gameEnded = false;
    
    // Reiniciar el texto del estado
    labelestado.setText("En partida");
    // Reiniciar el jugador actual
    this.currentPlayer = GameState.doesHumanStart() ? 
        GameState.getSelectedSymbol() : 
        (GameState.getSelectedSymbol() == GameTurns.X_TURNS) ? GameTurns.O_TURNS : GameTurns.X_TURNS;
    
    // Limpiar el tableros
    limpiarTablero(tablero);
    limpiarTablero(tableroMejor);
    limpiarTablero(tableroAnterior);
    actualizarTurno();
    // Si la IA empieza, hacer su movimiento
    if (!GameState.doesHumanStart()) {
        realizarMovimientoRandom();
    }
    }

    @FXML
    private void handlePauseResume(ActionEvent event) {
        isPaused = !isPaused;
    
    if (isPaused) {
        // Pausar el juego
        BtPausaresume.setText("Reanudar");
        // Deshabilitar el tablero
        tablero.setDisable(true);
    } else {
        // Reanudar el juego
       BtPausaresume.setText("Pausar");
        // Habilitar el tablero
        tablero.setDisable(false);
    }
        
    }
}
