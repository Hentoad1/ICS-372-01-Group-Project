package cafe.service;

import cafe.model.User;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private Map<String, User> users;
    private PersistenceService persistenceService;

    public AuthService(PersistenceService persistenceService) {
        this.users = new HashMap<>();
        this.persistenceService = persistenceService;
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public void initializeDefaultUsers() {
        addUser(new User("barista", "barista123", "BARISTA"));
        addUser(new User("manager", "manager123", "MANAGER"));
    }

    public void clear() {
        users.clear();
    }
}