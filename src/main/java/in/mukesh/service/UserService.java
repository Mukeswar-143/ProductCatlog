package in.mukesh.service;

import in.mukesh.entity.Users;
import in.mukesh.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepo repo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(Users user) {
        if (repo.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (repo.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(" Email already exists");
        }

        if (repo.existsByPnumber(user.getPnumber())) {
            throw new IllegalArgumentException(" Phone number already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public String verify(Users user) {
        try {
            Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(user.getUsername());
            }
        } catch (BadCredentialsException e) {
            return "Invalid username or password";
        } catch (Exception e) {
            return "Authentication failed: " + e.getMessage();
        }

        return "Authentication failed";
    }
}
