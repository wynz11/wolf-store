package devpro.example.CommerceAdmin.service;

import org.springframework.stereotype.Service;

import devpro.example.CommerceAdmin.model.Role;
import devpro.example.CommerceAdmin.repository.RoleRepository;

@Service
public class RoleService {
	
	private final RoleRepository rrp;
	
	public RoleService (RoleRepository rrp) {
		this.rrp = rrp;
	}
	
	public Role findByName(String name) {
		return rrp.findByName(name);
	}
}
