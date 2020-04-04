package art.soft.test.controller;

import art.soft.test.model.User;
import art.soft.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/{userid}")
    public User getUser(@PathVariable String userid) {
        return userService.search(userid);
    }

    @PostMapping("/modify/{userid}")
    public User modify(@PathVariable String userid,
                       @RequestParam(required = false) String login,
                       @RequestParam(required = false) String email,
                       @RequestParam(required = false) String password,
                       @RequestParam(required = false) Boolean active) {
        return userService.modify(userService.search(userid), login, email, password, active);
    }

    @PostMapping("/delete/{userid}")
    public User modify(@PathVariable String userid) {
        return userService.delete(userService.search(userid));
    }
}
