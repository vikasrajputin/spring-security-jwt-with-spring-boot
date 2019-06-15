package app.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import app.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomUserDetails implements UserDetails {

	private int id;
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	 
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
        
		
		return this.authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public static CustomUserDetails build(User user) {
		
		
		List<GrantedAuthority> tmpAuthority = new ArrayList<GrantedAuthority>();
		
		tmpAuthority.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		
		return new CustomUserDetails(user.getId(), user.getFirstName(), user.getLastName(), user.getUserName(), user.getPassword(), tmpAuthority);
	}

	public CustomUserDetails(int id, String firstName, String lastName, String userName, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.authorities = authorities;
	}
	
	

	

}
