package devpro.example.CommerceAdmin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import devpro.example.CommerceAdmin.model.User;
import devpro.example.CommerceAdmin.repository.UserRepository;

@Service
public class UserService {
	
	private final UserRepository urp;

	public UserService(UserRepository urp) {
		this.urp = urp;
	}
	
	public List<User> findAll(){
		return urp.findAll();
	}
	
	public User findById(Integer id) {
		return urp.findById(id).orElse(null);
	}
	
	public User findByUserName(String username) {
		return urp.findByUsername(username);
	}
	
	

}
