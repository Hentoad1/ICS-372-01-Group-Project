package cafe.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import cafe.model.*;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;

public class PersistenceService {
    private static final String DATA_DIR = "data/";
    private Gson gson;

    public PersistenceService() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        createDataDirectory();
    }

    private void createDataDirectory() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAllData(MenuService menuService, InventoryService inventoryService,
                            OrderService orderService, AuthService authService) {
        loadInventory(inventoryService);
        loadUsers(authService);

        if (menuService.getAllMenuItems().isEmpty()) {
            menuService.initializeDefaultMenu();
        }
        if (inventoryService.getAllInventory().isEmpty()) {
            inventoryService.initializeDefaultInventory();
        }
        if (authService.users.isEmpty()) {
            authService.initializeDefaultUsers();
        }
    }

    public void saveAllData(MenuService menuService, InventoryService inventoryService,
                            OrderService orderService) {
        saveInventory(inventoryService);
    }

    private void loadInventory(InventoryService inventoryService) {
        File file = new File(DATA_DIR + "inventory.json");
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Type type = new TypeToken<List<InventoryItem>>(){}.getType();
                List<InventoryItem> items = gson.fromJson(reader, type);
                if (items != null) {
                    inventoryService.clear();
                    items.forEach(inventoryService::addInventoryItem);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveInventory(InventoryService inventoryService) {
        try (Writer writer = new FileWriter(DATA_DIR + "inventory.json")) {
            gson.toJson(inventoryService.getAllInventory(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUsers(AuthService authService) {
        File file = new File(DATA_DIR + "users.json");
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Type type = new TypeToken<List<User>>(){}.getType();
                List<User> users = gson.fromJson(reader, type);
                if (users != null) {
                    authService.clear();
                    users.forEach(authService::addUser);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}