package devpro.example.CommerceAdmin.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_order")

public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	// Người đặt hàng
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	// Ngày đặt hàng
	@Column(name = "order_date")
	private LocalDate orderDate;

	// Tổng tiền
	@Column(name = "total_amount")
	private BigDecimal totalAmount;
	
	 // Phương thức thanh toán
    @Column(name = "payment_method")
    private String paymentMethod;

	// Trạng thái đơn hàng
	@Column(name = "status")
	private String status = "pending";
	
	@Column(name = "shipping_address")
	private String shippingAddress;
	
	@Column(name = "note")
	private String note;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Order(Integer id, User user, LocalDate orderDate, BigDecimal totalAmount, String paymentMethod,
			String status, String shippingAddress, String note) {
		super();
		this.id = id;
		this.user = user;
		this.orderDate = orderDate;
		this.totalAmount = totalAmount;
		this.paymentMethod = paymentMethod;
		this.status = status;
		this.shippingAddress = shippingAddress;
		this.note = note;
	}

	public Order() {
		super();
	}

	

	
}
