package devpro.example.CommerceAdmin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import devpro.example.CommerceAdmin.model.User;


public interface UserRepository extends JpaRepository<User, Integer>,
                                         JpaSpecificationExecutor<User>{
	
	Optional<User> findById(Integer id);
	
	public User findByUsername(String username);
	
	Optional<User> findByUsernameAndStatus(String username,
			Boolean status);
	
	boolean existsByUsername(String username);
	
}
