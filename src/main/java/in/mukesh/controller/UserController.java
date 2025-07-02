package in.mukesh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import in.mukesh.entity.ProductEntity;
import in.mukesh.entity.Users;
import in.mukesh.service.ProductService;
import in.mukesh.service.UserService;

@RestController
@RequestMapping("/customer")
public class UserController {

	@Autowired
	private UserService uService;

	@PostMapping("/register")
	public Users register(@RequestBody Users user) {
		user.setRole("ROLE_USER");
		return uService.register(user);
	}

	@PostMapping("/login")
	public String login(@RequestBody Users user) {
		return uService.verify(user);
	}

	@GetMapping("/dashboard")
	public String getDashboard() {
		return "Welcome to the User Dashboard!";
	}
}
