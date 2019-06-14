package todoapp.app.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import todoapp.app.beans.AuthenticationBean;
import todoapp.app.beans.CustomResponseBean;
import todoapp.app.beans.TodoBean;
import todoapp.app.dao.TodoDao;
import todoapp.app.security.token.TokenProvider;

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
	
	@RequestMapping(value={"/todo/create","/todo/update"},method=RequestMethod.POST,consumes="application/json",produces="application/json")
	public TodoBean createTodo(@RequestBody TodoBean bean){
		
		if(null != bean && (null == bean.getStatus() || "".equals(bean.getStatus()) )){
			bean.setStatus("Pending");
		}
		
		TodoBean todoBean = todoDao.save(bean);
		return todoBean;
	}
	
	@RequestMapping(value="/todo",produces="application/json")
	public List<TodoBean> getAll(){
				
		List<TodoBean> todoBeans = todoDao.findAll();
		return todoBeans;
	}
	
	@RequestMapping(value="/todo/delete/{id}")
	public CustomResponseBean deleteById(@PathVariable("id") String id){
				
		todoDao.delete(id);
		CustomResponseBean responseBean = new CustomResponseBean();
		responseBean.setMessage("Todo deleted successfully");
		responseBean.setStatusCode(1);
		return responseBean;
	}
	
	@RequestMapping(value="/todo/{id}",produces="application/json")
	public TodoBean getById(@PathVariable("id") String id){
				
		TodoBean todoBean = todoDao.findOne(id);
		return todoBean;
	}
	
	@RequestMapping(value="/todo/delete/all")
	public CustomResponseBean deleteAll(){
				
		todoDao.deleteAll();
		CustomResponseBean responseBean = new CustomResponseBean();
		responseBean.setMessage("Todo deleted successfully");
		responseBean.setStatusCode(1);
		return responseBean;
	}
	

	@PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationBean data) {
        try {
            String username = data.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            List<String> roles = new ArrayList<String>();
            roles.add("ADMIN");
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