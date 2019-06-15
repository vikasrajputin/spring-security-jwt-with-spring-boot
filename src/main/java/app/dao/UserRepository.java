package app.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	User findByUserName(String userName); 
}
