package todoapp.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import todoapp.app.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	User findByUserName(String userName); 
}
