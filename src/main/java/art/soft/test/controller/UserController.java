package art.soft.test.controller;

import art.soft.test.model.*;
import art.soft.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('ROLE_CLIENT')")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public List<UserInfo> getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/subscribe/{userid}")
    public String subscribe(HttpServletRequest req, @PathVariable String userid) {
        User user = userService.subscribe(req, userid);
        return "You successfully subscribe on " + user.getLogin() + "!";
    }

    @PostMapping("/unsubscribe/{userid}")
    public String unsubscribe(HttpServletRequest req, @PathVariable String userid) {
        User user = userService.unsubscribe(req, userid);
        return "You successfully unsubscribe on " + user.getLogin() + "!";
    }

    @GetMapping("/info/{userid}")
    public UserInfo getUser(@PathVariable String userid) {
        return new UserInfo(userService.search(userid));
    }
}
