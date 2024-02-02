package com.example.cs202pz.Scenes;

import com.example.cs202pz.Application;
import com.example.cs202pz.Common.Constants;
import com.example.cs202pz.Exceptions.InvalidPinException;
import com.example.cs202pz.DbConfig.DbManager;
import com.example.cs202pz.Interfaces.Resettable;
import com.example.cs202pz.Network.Client;
import com.example.cs202pz.Requests.ErrorLogRequest;
import com.example.cs202pz.Requests.LoginRequest;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import java.util.Objects;

/**
 * PinScene class represents the graphical user interface for entering a PIN in the ATM application.
 * It extends the JavaFX Scene class and implements the Resettable interface.
 */
public class PinScene extends Scene implements Resettable {
    /**
     * The instance of the PinScene used as a singleton.
     */
    public static final PinScene INSTANCE = new PinScene();
    private static String pin = "";
    private final PasswordField passwordField;

    /**
     * Constructs a PinScene with a VBox layout, a PasswordField, and a numeric grid.
     * Initializes event handlers for key presses.
     */
    public PinScene() {
        super(new VBox(10), Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());

        final VBox root = (VBox) getRoot();
        root.setAlignment(Pos.CENTER);

        passwordField = new PasswordField();
        passwordField.setAlignment(Pos.CENTER);
        passwordField.setMaxWidth(320);
        passwordField.setEditable(false);
        root.getChildren().add(passwordField);

        final GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        for (int i = 0; i < 12; i++) {
            Button button = getButton(i);
            grid.add(button, i % 3, i / 3);
        }

        root.getChildren().add(grid);

        super.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case ENTER -> confirmPin();
                case ESCAPE -> System.exit(0);
            }
        });

        String css = Objects.requireNonNull(getClass().getResource("/styles/PinScene.css")).toExternalForm();
        getStylesheets().add(css);
    }

    /**
     * Retrieves a Button based on the given index for the numeric grid.
     *
     * @param i The index used to determine the type of button to retrieve.
     * @return The Button object.
     */
    private Button getButton(int i) {
        Button button;

        switch (i) {
            case 9:
                ImageView buttonDeleteView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/assets/delete-left.png")).toExternalForm()));
                buttonDeleteView.setFitHeight(30);
                buttonDeleteView.setPreserveRatio(true);
                button = new Button();
                button.setGraphic(buttonDeleteView);
                button.setOnAction(e -> removeChar());
                break;
            case 10:
                button = new Button("0");
                button.setOnAction(e -> addCharToField('0'));
                break;
            case 11:
                ImageView buttonConfirmView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/assets/check.png")).toExternalForm()));
                buttonConfirmView.setFitHeight(30);
                buttonConfirmView.setPreserveRatio(true);
                button = new Button();
                button.setGraphic(buttonConfirmView);
                button.setOnAction(e -> confirmPin());
                break;
            default:
                button = new Button(Integer.toString(i + 1));
                button.setOnAction(e -> addCharToField(button.getText().charAt(0)));
                break;
        }

        button.setPrefHeight(100);
        button.setPrefWidth(100);

        return button;
    }

    /**
     * Adds a character to the PIN when a button is pressed.
     *
     * @param pinChar The character to add to the PIN.
     */
    private void addCharToField(Character pinChar) {
        if (pin.length() >= 4) {
            return;
        }

        pin += pinChar;
        setPin(pin);
    }

    /**
     * Removes the last character from the PIN when the delete button is pressed.
     */
    private void removeChar() {
        if (pin.isEmpty()) {
            return;
        }

        pin = pin.substring(0, pin.length() - 1);
        setPin(pin);
    }

    /**
     * Sets the displayed PIN in the PasswordField.
     *
     * @param pin The PIN to set.
     */
    private void setPin(String pin) {
        passwordField.setText(pin);
    }

    /**
     * Handles the PIN confirmation logic when the Enter key or Confirm button is pressed.
     */
    private void confirmPin() {
        try {
            if (pin.length() < 4) {
                throw new InvalidPinException();
            }

            Client.getHandler().send(new LoginRequest(pin));
            LoginRequest response = (LoginRequest) Client.getHandler().tryReceive();

            if (response.getId() == -1) {
                clear();
                throw new InvalidPinException();
            }

            DbManager.setAccountID(response.getId());
            Application.setScene(MainScene.INSTANCE);
        } catch (Exception ex) {
           try {
               Client.getHandler().send(new ErrorLogRequest(ex.getMessage()));
           } catch (Exception e) {}

            Application.Alert(Alert.AlertType.ERROR, Constants.error, ex.getMessage());
        }
    }

    /**
     * Clears the PIN and updates the PasswordField.
     */
    private void clear() {
        pin = "";
        setPin(pin);
    }

    /**
     * Resets the PIN and sets the account ID to -1.
     */
    @Override
    public void reset() {
        clear();
        DbManager.setAccountID(-1);
    }
}