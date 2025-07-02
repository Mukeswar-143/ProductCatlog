package in.mukesh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import in.mukesh.entity.Users;
import in.mukesh.service.UserService;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private UserService userService;

    

    @PostMapping("/register")
    public Users registerAdmin(@RequestBody Users user) {
        user.setRole("ROLE_ADMIN");
        return userService.register(user);
    }

    @PostMapping("/login")
    public String loginAdmin(@RequestBody Users user) {
        return userService.verify(user);
    }
}
