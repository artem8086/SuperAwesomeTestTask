package art.soft.test.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "verify")
public class VerificationToken {
    // id in mongoDB is token for verify
    @Id
    private String id;

    @DBRef
    private User user;

    public VerificationToken() {}

    public VerificationToken(User user) {
        this.user = user;
    }

    public String getToken() {
        return id;
    }

    public User getUser() {
        return user;
    }
}