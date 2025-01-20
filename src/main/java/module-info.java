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

    exports ec.edu.espol.singleplayertictactoe.ui;
    opens ec.edu.espol.singleplayertictactoe.ui to javafx.fxml;
}