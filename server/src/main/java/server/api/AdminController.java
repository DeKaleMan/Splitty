package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/validate_password")
    public ResponseEntity<String> validatePassword(@RequestParam("password") String password) {
        if (adminService.validatePassword(password)) {
            return ResponseEntity.ok("The password is correct");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The password is incorrect");
        }
    }

}
