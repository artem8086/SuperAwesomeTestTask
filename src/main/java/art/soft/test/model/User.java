package art.soft.test.model;

import art.soft.test.exception.CustomException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;

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
    private boolean isActive;

    @DBRef
    private Set<User> subscribes = new HashSet<User>();

    private String token;

    public User() {}

    public User(String login, String email, String password, boolean isAdmin) {
        if (login == null) throw new CustomException("User login is required!", HttpStatus.UNPROCESSABLE_ENTITY);
        if (email == null) throw new CustomException("User email is required!", HttpStatus.UNPROCESSABLE_ENTITY);
        if (password == null) throw new CustomException("User password is required!", HttpStatus.UNPROCESSABLE_ENTITY);
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
            roles.add(Role.ROLE_ADMIN);
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

    @JsonIgnore
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
