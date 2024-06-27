package org.example.clientapplication.controllers;

import lombok.extern.slf4j.Slf4j;
import org.example.clientapplication.services.UserAccountService;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/login")
public class LoginController {

    private final UserAccountService userAccountService;

    public LoginController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping()
    public String login(@RequestParam String email, @RequestParam String password) {
        log.info("login");
        if (userAccountService.checkUser(email, password)) {
            return "token";
        }
        return "error";
    }
}
