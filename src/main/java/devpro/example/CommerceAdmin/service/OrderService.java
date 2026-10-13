package devpro.example.CommerceAdmin.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import devpro.example.CommerceAdmin.dto.OrderDto;
import devpro.example.CommerceAdmin.model.Order;
import devpro.example.CommerceAdmin.model.User;
import devpro.example.CommerceAdmin.repository.OrderRepository;
import devpro.example.CommerceAdmin.repository.UserRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

@Service
public class OrderService {

	private final OrderRepository orp;
	private final UserRepository urp;

	public OrderService(OrderRepository orp, UserRepository urp) {
		this.orp = orp;
		this.urp = urp;
	}

	public long getTotalOrders() {
		return orp.count();
	}

	public long getProcessingOrders() {
		return orp.countByStatus("processing");
	}

	public long getCompletedOrders() {
		return orp.countByStatus("completed");
	}

	public long getCancelledOrders() {
		return orp.countByStatus("cancelled");
	}

	public List<Order> findAll() {
		return orp.findAll();
	}

	public Order findById(Integer id) {
		return orp.findById(id).orElse(null);
	}
	
	public Page<Order> search(
	        String keyword,
	        String status,
	        Pageable pageable) {

	    Specification<Order> spec = (root, query, cb) -> null;

	    // Tìm kiếm
	    if (keyword != null && !keyword.trim().isEmpty()) {

	        String kw = "%" + keyword.trim().toLowerCase() + "%";

	        spec = spec.and((root, q, cb) -> {

	            q.distinct(true);

	            List<Predicate> ors = new ArrayList<>();

	            // tìm theo ID
//	            try {
//	                Long id = Long.parseLong(keyword);
//	                ors.add(cb.equal(root.get("id"), id));
//	            } catch (Exception e) {
//	            }
	            
	            // Ma don hang
	            ors.add(
	                cb.like(
	                    cb.lower(root.get("id").as(String.class)),
	                    kw
	                )
	            );
	            
	            // Ten khach hang
	            Join<Order, User> userJoin = root.join("user");

	            ors.add(
	                cb.like(
	                    cb.lower(userJoin.get("username")),
	                    kw
	                )
	            );

	            return cb.or(ors.toArray(new Predicate[0]));
	        });
	    }

	    // lọc trạng thái
	    if (status != null && !status.isEmpty()) {

	        spec = spec.and((root, q, cb) ->
	            cb.equal(root.get("status"), status));
	    }

	    return orp.findAll(spec, pageable);
	}

	public Order insert(OrderDto orderDto) {

		Order order = new Order();

		order.setId(null);

		order.setOrderDate(orderDto.getOrderDate());

		order.setTotalAmount(orderDto.getTotalAmount());

		order.setPaymentMethod(orderDto.getPaymentMethod());

		order.setStatus(orderDto.getStatus());

		order.setShippingAddress(orderDto.getShippingAddress());

		order.setNote(orderDto.getNote());
		// user
		if (orderDto.getUserId() != null) {
			User user = urp.findById(orderDto.getUserId()).orElse(null);
			order.setUser(user);
		}

		return orp.save(order);
	}

	public Order update(OrderDto orderDto, Integer id) throws IOException {

		Order order = orp.findById(id).orElse(null);

		if (order == null) {
			return order;
		}

		order.setOrderDate(orderDto.getOrderDate());

		order.setTotalAmount(orderDto.getTotalAmount());

		order.setPaymentMethod(orderDto.getPaymentMethod());

		order.setStatus(orderDto.getStatus());

		order.setShippingAddress(orderDto.getShippingAddress());

		order.setNote(orderDto.getNote());
		// user
		if (orderDto.getUserId() != null) {
			User user = urp.findById(orderDto.getUserId()).orElse(null);
			order.setUser(user);
		}

		return orp.save(order);

	}

	// Inactive
	public void inactive(Integer id) {
		// Lay du lieu trong DB
		Order order = findById(id);
		if (order == null) {
			return;
		}
		order.setStatus("cancelled");
		orp.save(order);

		// Xoa hẳn
//				 orp.delete(order);
	}

}
