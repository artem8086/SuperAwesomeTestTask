package art.soft.test.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class User {
    @Id
    private String id;

    @Indexed(unique = true)
    private String login;
    @Indexed(unique = true)
    private String email;
    private String password;
    private boolean isAdmin;
    private boolean isActive;

    @DBRef
    private List<User> subscribes;

    public User() {}

    public User(String login, String email, String password, boolean isAdmin) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<User> getSubscribes() {
        return subscribes;
    }
}
