module ec.edu.espol.singleplayertictactoe {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens ec.edu.espol.singleplayertictactoe to javafx.fxml;
    exports ec.edu.espol.singleplayertictactoe;
}