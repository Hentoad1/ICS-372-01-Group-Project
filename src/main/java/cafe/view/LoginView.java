package cafe.view;

import cafe.controller.*;
import cafe.service.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginView {
    private Stage primaryStage;
    private AuthService authService;
    private MenuService menuService;
    private InventoryService inventoryService;
    private OrderService orderService;

    public LoginView(Stage primaryStage, AuthService authService, MenuService menuService,
                     InventoryService inventoryService, OrderService orderService) {
        this.primaryStage = primaryStage;
        this.authService = authService;
        this.menuService = menuService;
        this.inventoryService = inventoryService;
        this.orderService = orderService;
    }

    public void show() {
        primaryStage.setTitle("Brew & Bite Cafe System");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #6B3E2E, #8B5E3C);");

        VBox header = new VBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(30));
        Label titleLabel = new Label("Brew & Bite");
        titleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 48));
        titleLabel.setTextFill(Color.WHITE);
        Label subtitleLabel = new Label("Cafe Ordering System");
        subtitleLabel.setFont(Font.font("Georgia", 24));
        subtitleLabel.setTextFill(Color.web("#F5DEB3"));
        header.getChildren().addAll(titleLabel, subtitleLabel);

        VBox buttonPanel = new VBox(20);
        buttonPanel.setAlignment(Pos.CENTER);
        buttonPanel.setPadding(new Insets(40));

        Button customerBtn = createStyledButton("Continue as Customer", "#2E7D32");
        Button baristaBtn = createStyledButton("Barista Login", "#1565C0");
        Button managerBtn = createStyledButton("Manager Login", "#C62828");

        customerBtn.setOnAction(e -> showCustomerNameDialog());
        baristaBtn.setOnAction(e -> showLoginDialog("BARISTA"));
        managerBtn.setOnAction(e -> showLoginDialog("MANAGER"));

        buttonPanel.getChildren().addAll(customerBtn, baristaBtn, managerBtn);

        root.setTop(header);
        root.setCenter(buttonPanel);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createStyledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + color + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 18px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 15 30; " +
                "-fx-background-radius: 25;");
        return btn;
    }

    private void showCustomerNameDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Customer Name");
        dialog.setHeaderText("Enter Your Name");
        dialog.setContentText("Name:");

        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                CustomerController controller = new CustomerController(menuService, orderService, inventoryService);
                CustomerView customerView = new CustomerView(primaryStage, controller, name);
                customerView.show();
            }
        });
    }

    private void showLoginDialog(String role) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(role + " Login");
        dialog.setHeaderText("Enter credentials for " + role);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                var user = authService.authenticate(username.getText(), password.getText());
                if (user != null && user.getRole().equals(role)) {
                    if (role.equals("BARISTA")) {
                        BaristaController controller = new BaristaController(orderService);
                        BaristaView baristaView = new BaristaView(primaryStage, controller);
                        baristaView.show();
                    } else if (role.equals("MANAGER")) {
                        ManagerController controller = new ManagerController(menuService, inventoryService, orderService);
                        ManagerView managerView = new ManagerView(primaryStage, controller);
                        managerView.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid credentials!");
                    alert.showAndWait();
                }
            }
        });
    }
}