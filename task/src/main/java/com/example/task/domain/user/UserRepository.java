package com.example.task.domain.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
	
	Optional<User> findByUserId(String userId);
	
	Optional<User> findByRegNo(String regNo);
	
	Optional<User> findByName(String name);
}
