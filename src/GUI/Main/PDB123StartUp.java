package GUI.Main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Main class for PDB123.
// Loads the main stage.
public class PDB123StartUp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        PDB123Presenter pres1 = new PDB123Presenter(primaryStage);
        primaryStage.setTitle("PDB123");
        Scene primaryScene = new Scene(pres1.getPDB123View(), 600,400);
        primaryStage.setScene(primaryScene);
        primaryScene.getStylesheets().add("GUI/mainStyle.css");
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
