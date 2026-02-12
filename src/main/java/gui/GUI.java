package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class GUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("KarteikartenSystem");

        String path = "/design.fxml";

        URL fxmlUrl = GUI.class.getResource(path);
        System.out.println("Trying to load FXML from: " + path);
        System.out.println("Resolved URL: " + fxmlUrl);

        if (fxmlUrl == null) {
            throw new RuntimeException("FXML nicht gefunden: " + path +
                    "\nLiegt die Datei unter src/main/resources ?");
        }

        Parent root = FXMLLoader.load(fxmlUrl);

        // ✅ Scene zuerst erstellen
        Scene scene = new Scene(root);

        // ✅ Dark CSS laden
        URL cssUrl = GUI.class.getResource("/dark.css");
        System.out.println("Resolved CSS URL: " + cssUrl);

        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.out.println("dark.css NICHT gefunden!");
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
