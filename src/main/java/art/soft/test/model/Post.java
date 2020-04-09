package art.soft.test.model;

import art.soft.test.exception.CustomException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;

import java.util.Date;


@Document
public class Post {
    @Id
    private String id;

    @Indexed
    private String title;
    private String content;
    private Date date;

    @DBRef
    private User owner;

    public Post(String title, String content, User owner) {
        if (title == null) throw new CustomException("Post title is required!", HttpStatus.UNPROCESSABLE_ENTITY);
        if (content == null) throw new CustomException("Post content is required!", HttpStatus.UNPROCESSABLE_ENTITY);
        this.title = title;
        this.content = content;
        this.date = new Date();
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @JsonIgnore
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getUsername() {
        return owner.getLogin();
    }
}
