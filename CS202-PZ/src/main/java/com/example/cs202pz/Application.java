package com.example.cs202pz;

import com.example.cs202pz.Common.Constants;
import com.example.cs202pz.Network.Client;
import com.example.cs202pz.Scenes.PinScene;
import com.example.cs202pz.Interfaces.Resettable;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The main class representing the JavaFX application for the ATM system.
 * Extends javafx.application.Application to create a JavaFX application.
 */
public class Application extends javafx.application.Application {
    /**
     * The primary stage of the application.
     */
    private static Stage primaryStage;

    /**
     * Sets the scene for the primary stage using a Resettable scene. This method
     * ensures that the scene is reset, applied to the primary stage, and maximized.
     *
     * @param scene The Resettable scene to be set as the primary stage's scene.
     * @param <T>   A type parameter indicating that the scene must implement the Resettable interface.
     */
    public static <T extends Resettable> void setScene(T scene) {
        Platform.runLater(() -> {
            scene.reset();
            primaryStage.setScene((Scene) scene);
            if (!primaryStage.isMaximized()) {
                primaryStage.setMaximized(true);
                primaryStage.centerOnScreen();
            }
        });
    }

    /**
     * The entry point for the JavaFX application. Initiates the connection to the server,
     * sets up the primary stage, and displays the initial scene.
     *
     * @param stage The primary stage of the application.
     */
    @Override
    public void start(Stage stage) {
        try {
            Client.connect();
        } catch (Exception e) {
            Alert(Alert.AlertType.ERROR, Constants.error, Constants.unableToConnectToServer);
            return;
        }

        stage.setResizable(false);
        primaryStage = stage;
        stage.setTitle(Constants.atm);
        stage.initStyle(StageStyle.UNDECORATED);

        setScene(PinScene.INSTANCE);

        stage.show();
    }

    /**
     * Stops the application. Disconnects from the server before calling the superclass's stop method.
     *
     * @throws Exception If an exception occurs during the stopping process.
     */
    @Override
    public void stop() throws Exception {
        Client.disconnect();
        super.stop();
    }

    /**
     * Displays an alert with the specified type, title, and content text.
     *
     * @param type        The type of the alert.
     * @param title       The title of the alert.
     * @param contentText The content text of the alert.
     */
    public static void Alert(Alert.AlertType type, String title, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    /**
     * The main method to launch the JavaFX application.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        launch();
    }
}