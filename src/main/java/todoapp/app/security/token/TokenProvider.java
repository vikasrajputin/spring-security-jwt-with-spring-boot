package todoapp.app.security.token;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenProvider {

	
	@Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";
    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1h
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
	
	public String createToken(String username,List<String> roles){
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("roles", roles);
		
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + validityInMilliseconds);
		
		return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(expiryDate).signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}
	
	public String fetchToken(HttpServletRequest request){
		
		String tokenString = request.getHeader("Authorization");
		if(tokenString != null && tokenString.startsWith("Bearer")){
			return tokenString.substring(7, tokenString.length());
		}
		return null;
	}
	
	public boolean validateToken(String token){
		boolean isValid = true;
		
		
		try{
			Jws<Claims> jwsClaims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			
			if(jwsClaims.getBody().getExpiration().before(new Date())){
				return false;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return isValid;
	}
	
	public Authentication getAuthentication(HttpServletRequest request){
		
		String token = fetchToken(request);
		
		if(token != null && validateToken(token)){
			
			String username = getUserNameFromToken(token);
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			
			return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),userDetails.getAuthorities());
			
		
		}
		return null;
	}
	
	public String getUserNameFromToken(String token){
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

}
