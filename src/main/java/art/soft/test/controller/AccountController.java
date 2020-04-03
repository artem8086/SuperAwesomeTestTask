package art.soft.test.controller;

import art.soft.test.model.JwtToken;
import art.soft.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    public JwtToken signin(@RequestParam String login, @RequestParam String password) {
        return userService.signin(login, password);
    }

    @PostMapping("/signup")
    public JwtToken signup(@RequestParam String login, @RequestParam String email, @RequestParam String password) {
        return userService.signup(login, email, password);
    }
}
