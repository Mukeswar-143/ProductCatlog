package in.mukesh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.mukesh.entity.Users;
import in.mukesh.service.UserService;

@RestController
@RequestMapping("/admins")
@CrossOrigin(origins = {
    "http://localhost:3000",
    "http://localhost:3001",
    "https://shopverse-oubo.vercel.app"
})
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody Users user) {
        try {
            user.setRole("ROLE_ADMIN");
            Users savedAdmin = userService.register(user);
            return ResponseEntity.ok(savedAdmin);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to register admin");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody Users user) {
        String token = userService.verify(user);

        if (token.startsWith(" ")) {
            return ResponseEntity.status(401).body(token); // Unauthorized
        }

        return ResponseEntity.ok(token);
    }
}
