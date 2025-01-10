module ec.edu.espol.singleplayer.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    
    opens ec.edu.espol.singleplayer.tictactoe to javafx.fxml;
    exports ec.edu.espol.singleplayer.tictactoe;
}
