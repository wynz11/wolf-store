package devpro.example.CommerceAdmin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import devpro.example.CommerceAdmin.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>,
                           				JpaSpecificationExecutor<Order>{
	
	Optional<Order> findById(Integer id);
	
	List<Order> findByStatus(String status);
	
	List<Order> findByUserId(Integer userId);
	
	 long countByStatus(String status);

}
