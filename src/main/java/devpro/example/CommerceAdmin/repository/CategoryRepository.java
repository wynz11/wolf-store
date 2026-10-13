package devpro.example.CommerceAdmin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import devpro.example.CommerceAdmin.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>,
											JpaSpecificationExecutor<Category>{

	public Boolean existsByName(String name);
	
	Optional<Category> findById(Integer id);
	
	public List<Category> findByStatusTrue();
	
	  long countByStatus(Boolean status);
	
}
