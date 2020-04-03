package art.soft.test.controllers;

import art.soft.test.models.MessageObject;
import art.soft.test.models.User;
import art.soft.test.models.UserRepository;
import com.mongodb.DuplicateKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@RestController
public class UserController {

    @Autowired
    private UserRepository users;

    @PostMapping("/user/register")
    public MessageObject registerUser(@RequestParam String login, @RequestParam String email, @RequestParam String password) {
        try {
            users.insert(new User(login, email, password, false));
            return new MessageObject("User successfully registered!");
        } catch (DuplicateKeyException e) {
            return new MessageObject("User already exist in database!", true);
        } catch (Exception e) {
            return new MessageObject("Can't create new user!", true);
        }
    }

    @GetMapping("/user/all")
    public List<User> getUsers() {
        return users.findAll();
    }

    @GetMapping("/user/info/{userid}")
    public Object getUser(@PathVariable String userid) {
        try {
            return users.findById(userid).get();
        } catch (NoSuchElementException e) {}
        User user = users.findByLogin(userid);
        if (user != null) return user;
        return new MessageObject("User not found!", true);
    }
}
