package cafe.view;

import cafe.controller.ManagerController;
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

public class ManagerView {
    private Stage primaryStage;
    private ManagerController controller;
    private TabPane tabPane;
    private TableView<InventoryItem> inventoryTable;
    private ListView<MenuItem> menuList;

    public ManagerView(Stage primaryStage, ManagerController controller) {
        this.primaryStage = primaryStage;
        this.controller = controller;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #FFF3E0;");

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #C62828;");
        Label titleLabel = new Label("Brew & Bite - Manager Dashboard");
        titleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 24));
        titleLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white;");
        logoutBtn.setOnAction(e -> {
            primaryStage.close();
            new LoginView(primaryStage, null, null, null, null).show();
        });
        header.getChildren().addAll(titleLabel, logoutBtn);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);

        // Tab pane
        tabPane = new TabPane();

        // Inventory tab
        Tab inventoryTab = new Tab("Inventory Management", createInventoryPanel());
        inventoryTab.setClosable(false);

        // Menu tab
        Tab menuTab = new Tab("Menu Management", createMenuPanel());
        menuTab.setClosable(false);

        // Reports tab
        Tab reportsTab = new Tab("Reports", createReportsPanel());
        reportsTab.setClosable(false);

        tabPane.getTabs().addAll(inventoryTab, menuTab, reportsTab);

        root.setTop(header);
        root.setCenter(tabPane);

        // Add observer for real-time updates
        controller.addInventoryObserver(() -> Platform.runLater(() -> {
            refreshInventory();
        }));

        refreshInventory();

        Scene scene = new Scene(root, 1100, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Brew & Bite - Manager");
        primaryStage.show();
    }

    private VBox createInventoryPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));

        Label label = new Label("Current Inventory");
        label.setFont(Font.font("Georgia", FontWeight.BOLD, 18));

        inventoryTable = new TableView<>();

        TableColumn<InventoryItem, String> nameCol = new TableColumn<>("Item");
        nameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        nameCol.setPrefWidth(200);

        TableColumn<InventoryItem, Double> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getQuantity()).asObject());
        qtyCol.setPrefWidth(150);

        TableColumn<InventoryItem, String> unitCol = new TableColumn<>("Unit");
        unitCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUnit()));
        unitCol.setPrefWidth(100);

        TableColumn<InventoryItem, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setPrefWidth(300);
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final HBox buttons = new HBox(10);

            {
                Button restock1000 = new Button("+1000");
                Button restock500 = new Button("+500");
                Button restock100 = new Button("+100");
                restock1000.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                restock500.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                restock100.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                buttons.getChildren().addAll(restock1000, restock500, restock100);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    InventoryItem invItem = getTableView().getItems().get(getIndex());
                    // Recreate buttons with current item's action
                    Button restock1000 = new Button("+1000");
                    Button restock500 = new Button("+500");
                    Button restock100 = new Button("+100");
                    restock1000.setOnAction(e -> controller.restockItem(invItem.getName(), 1000));
                    restock500.setOnAction(e -> controller.restockItem(invItem.getName(), 500));
                    restock100.setOnAction(e -> controller.restockItem(invItem.getName(), 100));
                    HBox newButtons = new HBox(10, restock1000, restock500, restock100);
                    setGraphic(newButtons);
                }
            }
        });

        inventoryTable.getColumns().addAll(nameCol, qtyCol, unitCol, actionCol);
        inventoryTable.setPrefHeight(500);

        panel.getChildren().addAll(label, inventoryTable);
        return panel;
    }

    private VBox createMenuPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));

        Label label = new Label("Menu Items");
        label.setFont(Font.font("Georgia", FontWeight.BOLD, 18));

        menuList = new ListView<>();
        menuList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(MenuItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox container = new HBox(10);
                    container.setPadding(new Insets(5));
                    container.setAlignment(Pos.CENTER_LEFT);

                    Label nameLabel = new Label(item.getName() + " - $" + item.getPrice());
                    nameLabel.setPrefWidth(200);

                    Button removeBtn = new Button("Remove");
                    removeBtn.setStyle("-fx-background-color: #C62828; -fx-text-fill: white;");
                    removeBtn.setOnAction(e -> {
                        controller.removeMenuItem(item.getId());
                        refreshMenu();
                    });

                    container.getChildren().addAll(nameLabel, removeBtn);
                    setGraphic(container);
                }
            }
        });

        Button addItemBtn = new Button("Add New Item");
        addItemBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-weight: bold;");
        addItemBtn.setOnAction(e -> showAddItemDialog());

        panel.getChildren().addAll(label, menuList, addItemBtn);
        refreshMenu();
        return panel;
    }

    private VBox createReportsPanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(20));
        panel.setAlignment(Pos.TOP_CENTER);

        Label titleLabel = new Label("Sales Reports");
        titleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 20));

        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(20);
        statsGrid.setAlignment(Pos.CENTER);

        VBox totalSalesBox = new VBox(5);
        totalSalesBox.setAlignment(Pos.CENTER);
        totalSalesBox.setStyle("-fx-background-color: #E8F5E9; -fx-padding: 20; -fx-background-radius: 10;");
        Label totalSalesLabel = new Label("Total Sales");
        totalSalesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label totalSalesValue = new Label("$" + String.format("%.2f", controller.getTotalSales()));
        totalSalesValue.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        totalSalesValue.setTextFill(javafx.scene.paint.Color.GREEN);
        totalSalesBox.getChildren().addAll(totalSalesLabel, totalSalesValue);

        VBox ordersBox = new VBox(5);
        ordersBox.setAlignment(Pos.CENTER);
        ordersBox.setStyle("-fx-background-color: #E3F2FD; -fx-padding: 20; -fx-background-radius: 10;");
        Label ordersLabel = new Label("Orders Completed");
        ordersLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label ordersValue = new Label(String.valueOf(controller.getTotalOrdersFulfilled()));
        ordersValue.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        ordersValue.setTextFill(javafx.scene.paint.Color.BLUE);
        ordersBox.getChildren().addAll(ordersLabel, ordersValue);

        statsGrid.add(totalSalesBox, 0, 0);
        statsGrid.add(ordersBox, 1, 0);

        // Recent orders list
        Label recentLabel = new Label("Recent Fulfilled Orders");
        recentLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));

        ListView<Order> recentOrders = new ListView<>();
        recentOrders.setPrefHeight(300);
        recentOrders.getItems().addAll(controller.getFulfilledOrders());

        panel.getChildren().addAll(titleLabel, statsGrid, recentLabel, recentOrders);
        return panel;
    }

    private void refreshInventory() {
        inventoryTable.getItems().clear();
        inventoryTable.getItems().addAll(controller.getAllInventory());
    }

    private void refreshMenu() {
        menuList.getItems().clear();
        menuList.getItems().addAll(controller.getAllMenuItems());
    }

    private void showAddItemDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Menu Item");
        dialog.setHeaderText("Add New Item to Menu");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Beverage", "Pastry");
        typeCombo.setValue("Beverage");

        TextField nameField = new TextField();
        TextField priceField = new TextField();

        grid.add(new Label("Type:"), 0, 0);
        grid.add(typeCombo, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Base Price:"), 0, 2);
        grid.add(priceField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    String id = nameField.getText().toLowerCase().replace(" ", "_");
                    double price = Double.parseDouble(priceField.getText());
                    // Simplified - would need ingredient requirements in full implementation
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Item added! (Full implementation would add to menu)");
                    alert.showAndWait();
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid price!");
                    alert.showAndWait();
                }
            }
        });
    }
}