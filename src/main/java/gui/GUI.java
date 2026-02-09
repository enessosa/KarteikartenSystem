package gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    // diese Methode ist sogesehen die Main-Methode.
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("KarteikartenSystem");

        // button.setOnAction(e -> );
        Button buttonFrageAb = new Button();
        buttonFrageAb.setText("Abfragen");

        Button buttonErstelleDeck = new Button();
        buttonErstelleDeck.setText("Verwalte");


        StackPane layout = new StackPane();
        layout.getChildren().add(buttonFrageAb);
        StackPane.setAlignment(buttonFrageAb, Pos.BOTTOM_CENTER);
        StackPane.setMargin(buttonFrageAb, new Insets(30, 30, 30, 30));

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
        scene.getStylesheets().add("style.css");

        primaryStage.show();
    }

}
