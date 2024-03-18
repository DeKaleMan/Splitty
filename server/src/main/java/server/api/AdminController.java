package server.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Main;

import java.util.Objects;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/validate_password")
    public ResponseEntity<String> validatePassword(@RequestParam("password") String password) {
        if (Objects.equals(password, Main.password)) {
            return ResponseEntity.ok("The password is correct");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The password was incorrect");
        }
    }

}
