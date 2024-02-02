package com.example.cs202pz.Scenes;

import com.example.cs202pz.Application;
import com.example.cs202pz.Common.Constants;
import com.example.cs202pz.Common.Enums.TransactionStatus;
import com.example.cs202pz.DbConfig.DbManager;
import com.example.cs202pz.Entity.Transaction;
import com.example.cs202pz.Interfaces.Resettable;
import com.example.cs202pz.Network.Client;
import com.example.cs202pz.Requests.ErrorLogRequest;
import com.example.cs202pz.Requests.TransactionsRequest;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import java.util.ArrayList;
import java.util.Objects;

public class TransactionScene extends Scene implements Resettable {
    public static final TransactionScene INSTANCE = new TransactionScene();
    private final ArrayList<Transaction> transactionList = new ArrayList<>();
    private final TableView<Transaction> tableView = new TableView<>();

    public TransactionScene() {
        super(new VBox(10), Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
        TableColumn<Transaction, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Transaction, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Transaction, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Transaction, Double> amountBeforeColumn = new TableColumn<>("Amount Before");
        amountBeforeColumn.setCellValueFactory(new PropertyValueFactory<>("amountBefore"));

        TableColumn<Transaction, TransactionStatus> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.getColumns().addAll(idColumn, dateColumn, amountColumn, amountBeforeColumn, statusColumn);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tableView.setItems(FXCollections.observableArrayList());

        final VBox root = (VBox) getRoot();
        root.setAlignment(Pos.CENTER);

        final Label title = new Label(Constants.transactionHistory);
        title.setAlignment(Pos.CENTER);

        final Button button = new Button(Constants.back);
        button.setPrefWidth(320);
        button.setOnAction(e -> Application.setScene(MainScene.INSTANCE));

        root.getChildren().addAll(title, tableView, button);

        String css = Objects.requireNonNull(getClass().getResource("/styles/TransactionScene.css")).toExternalForm();
        getStylesheets().add(css);
    }

    @Override
    public void reset() {
        transactionList.clear();

        try {
            Client.getHandler().send(new TransactionsRequest(DbManager.getAccountID()));
            TransactionsRequest response = (TransactionsRequest) Client.getHandler().tryReceive();

            if (response == null) {
                return;
            }

            transactionList.addAll(response.getTransactions());

            for (Transaction transaction : transactionList) {
                tableView.getItems().add(transaction);
            }
        } catch (Exception ex) {
            try {
                Client.getHandler().send(new ErrorLogRequest(ex.getMessage()));
            } catch (Exception e) {}

            throw new RuntimeException(ex);
        }
    }
}