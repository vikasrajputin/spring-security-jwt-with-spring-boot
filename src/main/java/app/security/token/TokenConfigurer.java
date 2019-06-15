package app.security.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>{

	private TokenProvider tokenProvider;
	
	public TokenConfigurer(TokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}



	@Override
	public void configure(HttpSecurity builder) throws Exception {
		
		TokenFilter tokenFilter = new TokenFilter(tokenProvider);
		builder.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
	}

	
}
