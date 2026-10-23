package devpro.example.CommerceAdmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import devpro.example.CommerceAdmin.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer > {
	
	public Role findByName(String name);
}
