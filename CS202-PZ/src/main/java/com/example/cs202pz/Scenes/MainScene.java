package com.example.cs202pz.Scenes;

import com.example.cs202pz.Application;
import com.example.cs202pz.Common.Constants;
import com.example.cs202pz.DbConfig.DbManager;
import com.example.cs202pz.Interfaces.Resettable;
import com.example.cs202pz.Network.Client;
import com.example.cs202pz.Requests.ErrorLogRequest;
import com.example.cs202pz.Requests.UserRequest;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import java.util.Objects;

/**
 * Represents the main scene of the ATM application, extending JavaFX Scene and implementing the Resettable interface.
 * This scene displays user information, allows deposit, withdrawal, and access to transaction history.
 */
public class MainScene extends Scene implements Resettable {
    /**
     * The singleton instance of the MainScene class.
     */
    public static final MainScene INSTANCE = new MainScene();

    private final TextField nameField;
    private final TextField balanceField;

    /**
     * Constructs the MainScene, initializing UI elements and setting up event handlers.
     */
    public MainScene() {
        super(new VBox(10), Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());

        final VBox root = (VBox) getRoot();
        root.setAlignment(Pos.CENTER);

        nameField = new TextField();
        nameField.setAlignment(Pos.CENTER);
        nameField.setMaxWidth(320);
        nameField.setEditable(false);

        balanceField = new TextField();
        balanceField.setAlignment(Pos.CENTER);
        balanceField.setMaxWidth(320);
        balanceField.setEditable(false);

        final Button depositButton = new Button(Constants.deposit);
        depositButton.setPrefWidth(320);
        depositButton.setOnAction(e -> Application.setScene(DepositScene.INSTANCE));

        final Button withdrawButton = new Button(Constants.withdraw);
        withdrawButton.setPrefWidth(320);
        withdrawButton.setOnAction(e -> Application.setScene(WithdrawScene.INSTANCE));

        final Button transactionButton = new Button(Constants.transactions);
        transactionButton.setPrefWidth(320);
        transactionButton.setOnAction(e -> Application.setScene(TransactionScene.INSTANCE));

        final Button exitButton = new Button(Constants.back);
        exitButton.setPrefWidth(320);
        exitButton.setOnAction(e -> Application.setScene(PinScene.INSTANCE));

        root.getChildren().addAll(nameField, balanceField, depositButton, withdrawButton, transactionButton, exitButton);

        String css = Objects.requireNonNull(getClass().getResource("/styles/MainScene.css")).toExternalForm();
        getStylesheets().add(css);
    }

    /**
     * Resets the MainScene by updating the displayed user information (name and balance).
     * Invoked when returning to this scene.
     */
    @Override
    public void reset() {
        try {
            Client.getHandler().send(new UserRequest(DbManager.getAccountID()));
            UserRequest response = (UserRequest) Client.getHandler().tryReceive();
            nameField.setText(response.getUser().getFirstName() + " " + response.getUser().getLastName());
            balanceField.setText(String.valueOf(response.getUser().getBalance()) + '€');
        } catch (Exception ex) {
            try {
                Client.getHandler().send(new ErrorLogRequest(ex.getMessage()));
            } catch (Exception e) {}

            throw new RuntimeException(ex);
        }
    }
}