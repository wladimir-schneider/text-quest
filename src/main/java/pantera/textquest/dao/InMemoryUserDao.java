package pantera.textquest.dao;

import pantera.textquest.model.User;
import pantera.textquest.model.Role;
import pantera.textquest.model.Quest;

import java.util.*;

public class InMemoryUserDao {
    private final Map<String, User> users = Collections.synchronizedMap(new LinkedHashMap<>());
    private final Map<String, List<Quest>> userQuests = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, Quest> allQuests = Collections.synchronizedMap(new HashMap<>());

    public InMemoryUserDao() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setRole(Role.ADMIN);
        users.put(admin.getUsername(), admin);
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    public List<Quest> listQuests(String username) {
        return new ArrayList<>(userQuests.getOrDefault(username, List.of()));
    }

    public boolean addQuest(String username, Quest q) {
        if (username == null || q == null) return false;
        synchronized (userQuests) {
            userQuests.computeIfAbsent(username, k -> new ArrayList<>()).add(q);
            allQuests.put(q.getId(), q);
            return true;
        }
    }

    public Quest getQuest(String questId) {
        return allQuests.get(questId);
    }
    public boolean add(User u) {
        if (u == null || u.getUsername() == null) return false;
        synchronized (users) {
            if (users.containsKey(u.getUsername())) return false;
            users.put(u.getUsername(), u);
            return true;
        }
    }

    public boolean delete(String username) {
        return users.remove(username) != null;
    }

    public List<User> listAll() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> authenticate(String username, String password) {
        return findByUsername(username).filter(u -> Objects.equals(u.getPassword(), password));
    }
}
