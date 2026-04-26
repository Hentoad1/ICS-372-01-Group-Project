package cafe;

import cafe.service.*;
import cafe.view.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

public class CafeApp extends Application {
    private PersistenceService persistenceService;
    private MenuService menuService;
    private InventoryService inventoryService;
    private OrderService orderService;
    private AuthService authService;

    @Override
    public void start(Stage primaryStage) {
        persistenceService = new PersistenceService();
        menuService = new MenuService(persistenceService);
        inventoryService = new InventoryService(persistenceService);
        orderService = new OrderService(persistenceService, inventoryService);
        authService = new AuthService(persistenceService);

        persistenceService.loadAllData(menuService, inventoryService, orderService, authService);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            persistenceService.saveAllData(menuService, inventoryService, orderService);
        }));

        LoginView loginView = new LoginView(primaryStage, authService, menuService, inventoryService, orderService);
        loginView.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}