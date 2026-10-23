package devpro.example.CommerceAdmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import devpro.example.CommerceAdmin.dto.UserDto;
import devpro.example.CommerceAdmin.model.Role;
import devpro.example.CommerceAdmin.model.User;
import devpro.example.CommerceAdmin.service.RoleService;
import devpro.example.CommerceAdmin.service.UserService;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/signup")
	public String signup(final Model model) {
		model.addAttribute("userDto", new UserDto());
		return "signup";
	}
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@ModelAttribute UserDto userDto, Model model) {
		
		// Kiem tra username da ton tai
	    if (userService.existsByUsername(userDto.getUsername())) {
	        model.addAttribute("error", "Username da ton tai! Vui long chon ten khac.");
	        model.addAttribute("userDto", userDto);
	        return "signup";
	    }
		
		User user = new User();
		user.setId(null);
		user.setUsername(userDto.getUsername());
		user.setPassword(new BCryptPasswordEncoder(4).encode(userDto.getPassword()));
		user.setMobile(userDto.getMobile());
		
		//Set role cho user moi - mac dinh role la 'CUSTOMER'
		// Lay + role co name la 'CUSTOMER' trong DB
		Role role = roleService.findByName("CUSTOMER");
		user.addRelationalUserRole(role);
		userService.create(user);
		return "redirect:/login";
	}
}
