package inventorysystem.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The Main class for the application.
 * Javadocs are in the javadoc folder in the main directory (InventorySystem folder)
 * @author Alvin Roe
 */
public class Main extends Application {

    /**
     * Gets the first stage and scene up and running.
     * @param firstStage The window that the app runs on.
     * @throws IOException
     */
    @Override
    public void start(Stage firstStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/inventorysystem/view/MainForm.fxml"));
        Scene mainForm = new Scene(fxmlLoader.load(), 934, 347);
        firstStage.setTitle("Inventory System");
        firstStage.setScene(mainForm);

        firstStage.show();
    }

    /**
     * Changes the scene
     *
     * FUTURE ENHANCEMENT this should probably be move to its own class.
     *
     * @param viewFileLocation string for the view file location
     * @param nodeInScene a node in the current scene
     * @param title title of the stage
     * @param sceneWidth screen width
     * @param sceneHeight screen height
     * @param caller the object that is calling this method (utilized to get root)
     * @throws IOException
     */
    public static void changeScene(String viewFileLocation, Node nodeInScene, String title, double sceneWidth, double sceneHeight, Object caller) throws IOException {
        Parent root = FXMLLoader.load(caller.getClass().getResource(viewFileLocation));

        Stage stage = (Stage) nodeInScene.getScene().getWindow();

        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        stage.setTitle(title);

        stage.setScene(scene);

        stage.show();

    }

    /**
     * Launches the app
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
