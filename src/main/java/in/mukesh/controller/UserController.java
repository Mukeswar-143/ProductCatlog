package in.mukesh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.mukesh.entity.Users;
import in.mukesh.service.UserService;

@RestController
@RequestMapping("/customer")
@CrossOrigin(origins = {
		"http://localhost:3000",
		"http://localhost:3001",
		"https://shopverse-oubo.vercel.app"
})
public class UserController {

	@Autowired
	private UserService uService;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody Users user) {
		try {
			user.setRole("ROLE_USER");
			Users registeredUser = uService.register(user);
			return ResponseEntity.ok(registeredUser);
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Users user) {
		String token = uService.verify(user);
		return token.equals("fail") ? ResponseEntity.status(401).body("Invalid credentials") : ResponseEntity.ok(token);
	}

	@GetMapping("/dashboard")
	public String getDashboard() {
		return "Welcome to the User Dashboard!";
	}
}
