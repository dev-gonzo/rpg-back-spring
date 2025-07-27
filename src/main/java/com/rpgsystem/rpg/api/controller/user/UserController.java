package com.rpgsystem.rpg.api.controller.user;

import com.rpgsystem.rpg.domain.model.User;
import com.rpgsystem.rpg.infrastructure.security.AuthenticatedUserProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final AuthenticatedUserProvider userProvider;

    public UserController(AuthenticatedUserProvider userProvider) {
        this.userProvider = userProvider;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getLoggedUserInfo() {
        User user = userProvider.getAuthenticatedUser();

        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "isMaster", user.isMaster()
        ));
    }
}
