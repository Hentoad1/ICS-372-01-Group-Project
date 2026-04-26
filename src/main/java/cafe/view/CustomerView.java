package cafe.view;

import cafe.controller.CustomerController;
import cafe.model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class CustomerView {
    private Stage primaryStage;
    private CustomerController controller;
    private String customerName;
    private VBox cartPanel;
    private Label totalLabel;
    private ListView<HBox> menuListView;

    public CustomerView(Stage primaryStage, CustomerController controller, String customerName) {
        this.primaryStage = primaryStage;
        this.controller = controller;
        this.customerName = customerName;
        this.controller.setCustomerName(customerName);
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F5F5DC;");

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #6B3E2E;");
        Label titleLabel = new Label("Brew & Bite - Order for " + customerName);
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

        // Menu panel (left)
        VBox menuPanel = createMenuPanel();

        // Cart panel (right)
        cartPanel = createCartPanel();

        root.setTop(header);
        root.setLeft(menuPanel);
        root.setCenter(cartPanel);

        Scene scene = new Scene(root, 1200, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Brew & Bite - Customer Ordering");
        primaryStage.show();
    }

    private VBox createMenuPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setPrefWidth(500);
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label menuLabel = new Label("Menu");
        menuLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 20));

        Accordion accordion = new Accordion();

        // Beverages section
        VBox beveragesBox = new VBox(10);
        for (MenuItem item : controller.getBeverages()) {
            beveragesBox.getChildren().add(createMenuItemCard(item));
        }
        TitledPane beveragesPane = new TitledPane("Beverages", beveragesBox);

        // Pastries section
        VBox pastriesBox = new VBox(10);
        for (MenuItem item : controller.getPastries()) {
            pastriesBox.getChildren().add(createMenuItemCard(item));
        }
        TitledPane pastriesPane = new TitledPane("Pastries", pastriesBox);

        accordion.getPanes().addAll(beveragesPane, pastriesPane);

        panel.getChildren().addAll(menuLabel, accordion);
        return panel;
    }

    private HBox createMenuItemCard(MenuItem item) {
        HBox card = new HBox(10);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: #FFF8DC; -fx-background-radius: 5; -fx-border-color: #DEB887; -fx-border-radius: 5;");
        card.setAlignment(Pos.CENTER_LEFT);

        VBox infoBox = new VBox(5);
        Label nameLabel = new Label(item.getName());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label priceLabel = new Label(String.format("$%.2f", item.getPrice()));
        priceLabel.setTextFill(javafx.scene.paint.Color.GREEN);
        infoBox.getChildren().addAll(nameLabel, priceLabel);

        Spinner<Integer> quantitySpinner = new Spinner<>(1, 10, 1);
        quantitySpinner.setEditable(true);

        Button addBtn = new Button("Add to Cart");
        addBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white;");
        addBtn.setOnAction(e -> {
            controller.addToCart(item, quantitySpinner.getValue());
            updateCart();
        });

        HBox.setHgrow(infoBox, Priority.ALWAYS);
        card.getChildren().addAll(infoBox, quantitySpinner, addBtn);
        return card;
    }

    private VBox createCartPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: #FFF8DC; -fx-background-radius: 10;");

        Label cartLabel = new Label("Current Order");
        cartLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 20));

        VBox itemsBox = new VBox(10);
        ScrollPane scrollPane = new ScrollPane(itemsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);

        totalLabel = new Label("Total: $0.00");
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        Button clearBtn = new Button("Clear Order");
        clearBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        clearBtn.setOnAction(e -> {
            controller.clearCart();
            updateCart();
        });

        Button placeBtn = new Button("Place Order");
        placeBtn.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; -fx-font-weight: bold;");
        placeBtn.setOnAction(e -> {
            if (controller.placeOrder(customerName)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order placed successfully!");
                alert.showAndWait();
                updateCart();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to place order. Cart is empty or insufficient inventory!");
                alert.showAndWait();
            }
        });

        buttonBox.getChildren().addAll(clearBtn, placeBtn);
        panel.getChildren().addAll(cartLabel, scrollPane, totalLabel, buttonBox);

        return panel;
    }

    private void updateCart() {
        VBox itemsBox = (VBox) ((ScrollPane) cartPanel.getChildren().get(1)).getContent();
        itemsBox.getChildren().clear();

        for (OrderItem item : controller.getCurrentCart()) {
            HBox itemRow = new HBox(10);
            itemRow.setPadding(new Insets(5));
            itemRow.setAlignment(Pos.CENTER_LEFT);

            Label nameLabel = new Label(item.getMenuItem().getName());
            nameLabel.setPrefWidth(150);

            Spinner<Integer> qtySpinner = new Spinner<>(1, 20, item.getQuantity());
            qtySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
                controller.updateCartQuantity(item.getMenuItem(), newVal);
                updateCart();
            });

            Label priceLabel = new Label(String.format("$%.2f", item.getTotalPrice()));
            priceLabel.setPrefWidth(80);

            Button removeBtn = new Button("Remove");
            removeBtn.setStyle("-fx-background-color: #C62828; -fx-text-fill: white;");
            removeBtn.setOnAction(e -> {
                controller.removeFromCart(item.getMenuItem());
                updateCart();
            });

            itemRow.getChildren().addAll(nameLabel, qtySpinner, priceLabel, removeBtn);
            itemsBox.getChildren().add(itemRow);
        }

        totalLabel.setText(String.format("Total: $%.2f", controller.getCartTotal()));
    }
}