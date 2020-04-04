package art.soft.test.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private boolean isActive = true;

    @DBRef
    private Set<User> subscribes = new HashSet<User>();

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

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @JsonIgnore
    public List<Role> getRoles() {
        ArrayList<Role> roles = new ArrayList<Role>();
        roles.add(Role.ROLE_CLIENT);
        if (isAdmin) {
            roles.add(Role.ROLE_CLIENT);
        }
        return roles;
    }

    @JsonIgnore
    public Set<User> getSubscribes() {
        return subscribes;
    }

    @JsonIgnore
    public List<com.mongodb.DBRef> getSubsRef() {
        return subscribes.stream().map(u -> new com.mongodb.DBRef("user", new ObjectId(u.id))).collect(Collectors.toList());
    }

    public List<String> getSubs() {
        return subscribes.stream().map(u -> u.login).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof User)) return false;
        User u = (User) o;
        return id.equals(u.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
