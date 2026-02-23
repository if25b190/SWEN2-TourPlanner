package at.fhtw.tourplanner.controller;

import at.fhtw.tourplanner.dto.LoginDto;
import at.fhtw.tourplanner.model.Account;
import at.fhtw.tourplanner.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PROTECTED)
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Account> register(@Valid @RequestBody LoginDto loginDto) {
        var result = userService.registerUser(loginDto);
        return result.map(account -> ResponseEntity.created(URI.create("/api/v1/profile")).body(account))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/profile")
    public ResponseEntity<String> profile(Authentication authentication) {
        return ResponseEntity.ok(authentication.getName());
    }
}
