package in.mukesh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.mukesh.entity.Users;

public interface UserRepo extends JpaRepository<Users, Integer> {
	Users findByUsername(String username);

}
