package art.soft.test.model;

public class UserInfo {
    private String id;

    private String username;

    public UserInfo(User user) {
        id = user.getId();
        username = user.getLogin();
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
