package devpro.example.CommerceAdmin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import devpro.example.CommerceAdmin.model.OrderStatus;
import devpro.example.CommerceAdmin.repository.OrderStatusRepository;


@Service
public class OrderStatusSerVice {
	private final OrderStatusRepository osr;
	
	public OrderStatusSerVice(OrderStatusRepository osr) {
		this.osr = osr;
	}
	
	public OrderStatus findByName(String name) {
		return osr.findByName(name);
	}
	
	public List<OrderStatus> findAll(){
		return osr.findAll();
	}
	
}
