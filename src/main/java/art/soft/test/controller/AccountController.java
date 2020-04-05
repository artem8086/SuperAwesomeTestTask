package art.soft.test.controller;

import art.soft.test.model.JwtToken;
import art.soft.test.model.VerificationToken;
import art.soft.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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
    public String signup(UriComponentsBuilder builder,
                      @RequestParam String login,
                      @RequestParam String email,
                      @RequestParam String password) {
        VerificationToken token = userService.signup(login, email, password);
        return builder.path("/account/confirm").query("token={token}").buildAndExpand(token.getToken()).toUri().toString();
    }

    @RequestMapping("/confirm")
    public JwtToken confirm(@RequestParam String token) {
        return userService.confirm(token);
    }
}
