package in.mukesh.service;

import in.mukesh.entity.*;
import in.mukesh.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("User Not Found");
        return new UserPrincipal(user);
    }
}
