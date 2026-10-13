package devpro.example.CommerceAdmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import devpro.example.CommerceAdmin.model.OrderStatus;


public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
	
	public OrderStatus findByName(String name);
}
