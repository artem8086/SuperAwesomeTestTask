package art.soft.test.model;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;

public class UserSubscribe {
    private Date date;

    @DBRef
    private User user;

    public UserSubscribe(User user) {
        this.user = user;
        date = new Date();
    }

    public Date getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o instanceof User) {
            User u = (User) o;
            return user.getId().equals(u.getId());
        }
        if (!(o instanceof UserSubscribe)) return false;
        UserSubscribe u = (UserSubscribe) o;
        return user.getId().equals(u.user.getId());
    }

    @Override
    public int hashCode() {
        return user.getId().hashCode();
    }
}
