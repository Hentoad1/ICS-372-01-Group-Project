package cafe.view;

import cafe.controller.BaristaController;
import cafe.model.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class BaristaView {
    private Stage primaryStage;
    private BaristaController controller;
    private ListView<Order> pendingList;
    private ListView<Order> fulfilledList;

    public BaristaView(Stage primaryStage, BaristaController controller) {
        this.primaryStage = primaryStage;
        this.controller = controller;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #E8F5E9;");

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #1565C0;");
        Label titleLabel = new Label("Brew & Bite - Barista Dashboard");
        titleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 24));
        titleLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #C62828; -fx-text-fill: white;");
        logoutBtn.setOnAction(e -> {
            primaryStage.close();
            new LoginView(primaryStage, null, null, null, null).show();
        });
        header.getChildren().addAll(titleLabel, logoutBtn);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        // Main content
        SplitPane splitPane = new SplitPane();

        // Pending orders
        VBox pendingPanel = createPendingPanel();

        // Fulfilled orders
        VBox fulfilledPanel = createFulfilledPanel();

        splitPane.getItems().addAll(pendingPanel, fulfilledPanel);

        root.setTop(header);
        root.setCenter(splitPane);

        // Add observer for real-time updates
        controller.addOrderObserver(() -> Platform.runLater(() -> {
            refreshOrders();
        }));

        refreshOrders();

        Scene scene = new Scene(root, 1200, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Brew & Bite - Barista");
        primaryStage.show();
    }

    private VBox createPendingPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: white;");

        Label label = new Label("Pending Orders");
        label.setFont(Font.font("Georgia", FontWeight.BOLD, 18));

        pendingList = new ListView<>();
        pendingList.setCellFactory(param -> new OrderCell(true));
        pendingList.setPrefHeight(500);

        panel.getChildren().addAll(label, pendingList);
        return panel;
    }

    private VBox createFulfilledPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: #F5F5F5;");

        Label label = new Label("Fulfilled Orders");
        label.setFont(Font.font("Georgia", FontWeight.BOLD, 18));

        fulfilledList = new ListView<>();
        fulfilledList.setCellFactory(param -> new OrderCell(false));
        fulfilledList.setPrefHeight(500);

        panel.getChildren().addAll(label, fulfilledList);
        return panel;
    }

    private void refreshOrders() {
        pendingList.getItems().clear();
        pendingList.getItems().addAll(controller.getPendingOrders());

        fulfilledList.getItems().clear();
        fulfilledList.getItems().addAll(controller.getFulfilledOrders());
    }

    private class OrderCell extends ListCell<Order> {
        private boolean isPending;

        OrderCell(boolean isPending) {
            this.isPending = isPending;
        }

        @Override
        protected void updateItem(Order order, boolean empty) {
            super.updateItem(order, empty);
            if (empty || order == null) {
                setText(null);
                setGraphic(null);
            } else {
                VBox container = new VBox(5);
                container.setPadding(new Insets(10));
                container.setStyle("-fx-background-color: #FFF8DC; -fx-background-radius: 5;");

                HBox headerRow = new HBox(20);
                Label idLabel = new Label("Order #" + order.getOrderId());
                idLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                Label nameLabel = new Label("Customer: " + order.getCustomerName());
                Label statusLabel = new Label(order.getStatus().getDisplayName());
                statusLabel.setTextFill(order.getStatus() == OrderStatus.PENDING ? javafx.scene.paint.Color.ORANGE :
                        order.getStatus() == OrderStatus.IN_PROGRESS ? javafx.scene.paint.Color.BLUE :
                        javafx.scene.paint.Color.GREEN);
                headerRow.getChildren().addAll(idLabel, nameLabel, statusLabel);

                VBox itemsBox = new VBox(2);
                for (OrderItem item : order.getItems()) {
                    itemsBox.getChildren().add(new Label(item.getQuantity() + "x " + item.getMenuItem().getName() +
                            " - $" + String.format("%.2f", item.getTotalPrice())));
                }

                Label totalLabel = new Label("Total: $" + String.format("%.2f", order.getTotalPrice()));
                totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

                container.getChildren().addAll(headerRow, itemsBox, totalLabel);

                if (isPending && order.getStatus() != OrderStatus.FULFILLED) {
                    HBox buttonBox = new HBox(10);
                    ComboBox<OrderStatus> statusCombo = new ComboBox<>();
                    statusCombo.getItems().addAll(OrderStatus.PENDING, OrderStatus.IN_PROGRESS, OrderStatus.READY_FOR_PICKUP, OrderStatus.FULFILLED);
                    statusCombo.setValue(order.getStatus());
                    statusCombo.setOnAction(e -> {
                        controller.updateOrderStatus(order.getOrderId(), statusCombo.getValue());
                    });
                    buttonBox.getChildren().add(statusCombo);
                    container.getChildren().add(buttonBox);
                }

                setGraphic(container);
            }
        }
    }
}