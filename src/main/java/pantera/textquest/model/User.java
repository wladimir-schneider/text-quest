package pantera.textquest.model;

import java.util.UUID;

public class User {
    private final String id = UUID.randomUUID().toString();
    private String username;
    private String password;
    private Role role = Role.USER;
    private String progress = "start";

    public User() {
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }
}
