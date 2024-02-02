package com.example.cs202pz.Scenes;

import com.example.cs202pz.Application;
import com.example.cs202pz.Common.Constants;
import com.example.cs202pz.Common.Enums.TransactionStatus;
import com.example.cs202pz.Exceptions.WithdrawException;
import com.example.cs202pz.DbConfig.DbManager;
import com.example.cs202pz.Interfaces.Resettable;
import com.example.cs202pz.Network.Client;
import com.example.cs202pz.Requests.DepositRequest;
import com.example.cs202pz.Requests.ErrorLogRequest;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import java.util.Objects;

/**
 * Represents the scene for withdrawing money in the ATM application, extending JavaFX Scene and implementing the Resettable interface.
 * Users can input the withdrawal amount, clear the input, and complete the withdrawal transaction.
 */
public class WithdrawScene extends Scene implements Resettable {
    /**
     * The singleton instance of the WithdrawScene class.
     */
    public static final WithdrawScene INSTANCE = new WithdrawScene();
    private static String amount = "";
    private final TextField textField;

    /**
     * Constructs the WithdrawScene, initializing UI elements and setting up event handlers.
     */
    public WithdrawScene() {
        super(new VBox(10), Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());

        final VBox root = (VBox) getRoot();
        root.setAlignment(Pos.CENTER);

        textField = new TextField();
        textField.setAlignment(Pos.CENTER);
        textField.setMaxWidth(320);
        textField.setEditable(false);
        root.getChildren().addAll(new Label(Constants.amount + ": "), textField);

        final GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        for (int i = 0; i < 12; i++) {
            Button button = getButton(i);
            grid.add(button, i % 3, i / 3);
        }

        root.getChildren().add(grid);

        final Button clearButton = new Button(Constants.clear);
        clearButton.setPrefWidth(320);
        clearButton.setOnAction(e -> {
            clear();
            textField.requestFocus();
        });

        final Button depositButton = getButton();
        depositButton.setPrefWidth(320);
        final Button back = new Button(Constants.back);
        back.setPrefWidth(320);
        back.setOnAction(e -> Application.setScene(MainScene.INSTANCE));

        root.getChildren().addAll(depositButton, clearButton, back);

        String css = Objects.requireNonNull(getClass().getResource("/styles/DepositScene.css")).toExternalForm();
        getStylesheets().add(css);
    }

    /**
     * Creates and returns a button for the numeric input grid based on the given index.
     *
     * @param i The index representing the button's position in the numeric grid.
     * @return The Button for the specified index with appropriate event handling.
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
                button = new Button(".");
                button.setOnAction(e -> addCharToField('.'));
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
     * Adds the given character to the withdrawal amount field after validation.
     *
     * @param amountChar The character to be added to the withdrawal amount.
     */
    private void addCharToField(Character amountChar) {
        if (!validateCharField(amountChar)) return;

        amount += amountChar;
        setAmount(amount);
    }

    /**
     * Removes the last character from the withdrawal amount field.
     * Does nothing if the withdrawal amount is already empty.
     */
    private void removeChar() {
        if (amount.isEmpty()) {
            return;
        }

        amount = amount.substring(0, amount.length() - 1);
        setAmount(amount);
    }

    /**
     * Sets the withdrawal amount in the text field.
     *
     * @param amount The withdrawal amount to be displayed.
     */
    private void setAmount(String amount) {
        textField.setText(amount);
    }

    /**
     * Retrieves and returns a button for the withdrawal action with appropriate event handling.
     *
     * @return The Button for the withdrawal action.
     */
    private Button getButton() {
        final Button button = new Button(Constants.withdraw);
        button.setOnAction(e -> {
            try {
                final double amount = Double.parseDouble(textField.getText());
                Client.getHandler().send(new DepositRequest(DbManager.getAccountID(), amount, true));

                DepositRequest response = (DepositRequest) Client.getHandler().tryReceive();
                if (response.getStatus() == TransactionStatus.DECLINED) {
                    throw new WithdrawException(Constants.withdrawDeclined);
                }

                Application.Alert(Alert.AlertType.INFORMATION, Constants.withdraw, Constants.withdrawSuccessful);
            } catch (Exception ex) {
                try {
                    Client.getHandler().send(new ErrorLogRequest(ex.getMessage()));
                } catch (Exception exc) {}

                Application.Alert(Alert.AlertType.ERROR, Constants.error, ex.getMessage());
                return;
            }

            Application.setScene(MainScene.INSTANCE);
        });

        return button;
    }

    /**
     * Validates whether the given character can be added to the withdrawal amount field.
     *
     * @param amountChar The character to be validated.
     * @return True if the character is valid for addition, false otherwise.
     */
    private boolean validateCharField(Character amountChar) {
        if ((amountChar.equals('.') && amount.isEmpty()) ||
                (amount.contains(".") && amountChar.equals('.') ||
                (amountChar.equals('0') && amount.isEmpty())) ||
                (amount.contains(".") && amount.indexOf('.') + 4 == amount.length())) {
            return false;
        }

        return true;
    }

    /**
     * Clears the withdrawal amount, resetting it to an empty state.
     */
    private void clear() {
        amount = "";
        setAmount(amount);
    }

    /**
     * Resets the WithdrawScene by clearing the accumulated withdrawal amount.
     * Invoked when returning to this scene.
     */
    @Override
    public void reset() {
        clear();
    }
}