package com.rpgsystem.rpg.api.controller.user;

import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.AuthenticatedUserProvider;
import com.rpgsystem.rpg.infrastructure.security.annotation.RequireAuthUser;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
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
    @RequireAuthUser
    public ResponseEntity<?> getLoggedUserInfo() {
        User user = userProvider.getAuthenticatedUser();
        
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "isMaster", user.isMaster()
        ));
    }
}
