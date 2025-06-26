package in.mukesh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.mukesh.entity.Users;
import in.mukesh.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService uService;

	@PostMapping("/register")
	public Users register(@RequestBody Users user) {
		uService.register(user);
		return user;
	}

	@PostMapping("/login")
	public String login(@RequestBody Users user) {

		return uService.verify(user);
	}
}
