package app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.beans.AuthenticationBean;
import app.beans.CustomResponseBean;
import app.dao.TodoDao;
import app.entity.Todo;
import app.security.token.TokenProvider;

@RestController
@CrossOrigin(origins="*")
public class TodoController {

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	TokenProvider tokenProvider;
	
	@Autowired
	TodoDao todoDao;
	
	
	@RequestMapping("/hello")
	public String sayHello(){
		
		return "Hello";
	}
	
	//@RequestMapping(value="/todo",method=RequestMethod.POST,consumes="application/json",produces="application/json")
	@PostMapping(value="/todo")
	public Todo createTodo(@RequestBody Todo bean){
		
		Todo todoBean = todoDao.save(bean);
		return todoBean;
	}
	
	/*@RequestMapping(value="/todo",produces="application/json")*/
	@GetMapping(value="/todo")
	public List<Todo> getAll(){
				
		List<Todo> todoBeans = todoDao.findAll();
		return todoBeans;
	}
	
	//@RequestMapping(value="/todo/{id}",method)
	@DeleteMapping(value="/todo/{id}")
	public CustomResponseBean deleteById(@PathVariable("id") String id){
				
		todoDao.delete(id);
		CustomResponseBean responseBean = new CustomResponseBean();
		responseBean.setMessage("Todo deleted successfully");
		responseBean.setStatusCode(1);
		return responseBean;
	}
	
	@GetMapping(value="/todo/{id}")
	public Todo getById(@PathVariable("id") String id){
				
		Todo todoBean = todoDao.findOne(id);
		return todoBean;
	}

	@PutMapping(value="/todo/{id}")
	public Todo updateTodo(@RequestBody Todo bean,@PathVariable("id") Integer id){
		bean.setId(id);
		Todo todoBean = todoDao.save(bean);
		return todoBean;
	}

	@PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationBean data) {
        try {
            String username = data.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            List<String> roles = new ArrayList<String>();
            roles.add("ROLE_ADMIN");
            String token = tokenProvider.createToken(username, roles);
            Map<Object, Object> model = new HashMap();
            model.put("username", username);
            model.put("token", token);
            return ResponseEntity.ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }
}