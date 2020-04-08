package art.soft.test.controller;

import art.soft.test.dto.UserDTO;
import art.soft.test.model.JwtToken;
import art.soft.test.model.VerificationToken;
import art.soft.test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    public JwtToken signin(@RequestBody UserDTO user) {
        return userService.signin(user);
    }

    @PostMapping("/signup")
    public String signup(UriComponentsBuilder builder, @RequestBody UserDTO user) {
        VerificationToken token = userService.signup(user);
        return builder.path("/account/confirm").query("token={token}").buildAndExpand(token.getToken()).toUri().toString();
    }

    @RequestMapping("/confirm")
    public JwtToken confirm(@RequestParam String token) {
        return userService.confirm(token);
    }
}
