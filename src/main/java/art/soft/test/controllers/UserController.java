package art.soft.test.controllers;

import art.soft.test.config.JwtTokenProvider;
import art.soft.test.models.JwtToken;
import art.soft.test.models.MessageObject;
import art.soft.test.models.User;
import art.soft.test.models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository users;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signin")
    public Object signin(@RequestParam String login, @RequestParam String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
            return new JwtToken(jwtTokenProvider.createToken(login, users.findByLogin(login).getRoles()));
        } catch (AuthenticationException e) {
            return new MessageObject("Invalid login/password supplied", true);
        }
    }

    @PostMapping("/signup")
    public Object signup(@RequestParam String login, @RequestParam String email, @RequestParam String password) {
        try {
            User user = new User(login, email, password, false);
            String token = jwtTokenProvider.createToken(login, user.getRoles());
            users.insert(user);
            return new JwtToken(token);
        } catch (DuplicateKeyException e) {
            return new MessageObject("Login or email already taken!", true);
        } catch (Exception e) {
            return new MessageObject("Can't create new user!", true);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public List<User> getUsers() {
        return users.findAll();
    }

    @PostMapping("/subscribe/{userid}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public MessageObject subscribe(@PathVariable String userid, @RequestParam String user) {
        try {
            User userCur = findUser(user);
            User userSub = findUser(userid);
            if (userCur.equals(userSub)) {
                return new MessageObject("You can't subscribe on yourself!", true);
            }

            if (userCur.getSubscribes().add(userSub)) {
                users.save(userCur);
            } else {
                return new MessageObject("You already subscribe on this user!");
            }
            return new MessageObject("User successfully subscribe!");
        } catch (NoSuchElementException e) {
            return new MessageObject("User not found!", true);
        }
    }

    @GetMapping("/info/{userid}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Object getUser(@PathVariable String userid) {
        try {
            return findUser(userid);
        } catch (NoSuchElementException e) {
            return new MessageObject("User not found!", true);
        }
    }

    private User findUser(String userId) {
        try {
            return users.findById(userId).get();
        } catch (NoSuchElementException e) {}
        User user = users.findByLogin(userId);
        if (user != null) return user;
        throw new NoSuchElementException();
    }
}
