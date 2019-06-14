package todoapp.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.lang.Collections;
import todoapp.app.beans.CustomUserDetails;
import todoapp.app.dao.UserRepository;
import todoapp.app.entity.User;

@Service("userService")
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	
	
	@Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

	 
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		
		User user = userRepository.findByUserName(username);
		
		if(user == null){
			throw new UsernameNotFoundException(username + " Not Found");
		}
		
		return CustomUserDetails.build(user);
	}

}
