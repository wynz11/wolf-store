package devpro.example.CommerceAdmin.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_sale_order")
public class SaleOrder {
	
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    @Column(name = "code")
	    private String code;

	    @Column(name = "total")
	    private BigDecimal total;

	    @Column(name = "customer_name")
	    private String customerName;

	    @Column(name = "customer_mobile")
	    private String customerMobile;

	    @Column(name = "customer_email")
	    private String customerEmail;

	    @Column(name = "customer_address")
	    private String customerAddress;

	    @Column(name = "create_date")
	    private LocalDate createDate;

	    @Column(name = "update_date")
	    private LocalDate updateDate;

	    @Column(name = "delivery_date")
	    private LocalDate deliveryDate;

	    @Column(name = "status")
	    private Boolean status;

	    @Column(name = "description")
	    private String description;
	    
		//Mapping one-to-many: tbl_sale_order-to-tbl-sale_order_product
		@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
				mappedBy = "saleOrder")
		private List<SaleOrderProduct> saleOrderProducts =
				new ArrayList<SaleOrderProduct>();
		
		// Add and remove element out of sales-order-product list
		public void addRelationalSaleOrderProduct(SaleOrderProduct saleOrderProduct) {
			saleOrderProducts.add(saleOrderProduct);
			saleOrderProduct.setSaleOrder(this);
		}
		public void removeRelationalSaleOrderProduct(SaleOrderProduct saleOrderProduct) {
			saleOrderProducts.remove(saleOrderProduct);
			saleOrderProduct.setSaleOrder(this);
		}
	    
	 // cấu hình quan hệ
		// Mapping many-to-one: tbl_sale_order-to-tbl_user
		@ManyToOne(fetch = FetchType.EAGER)
		@JoinColumn(name = "user_id", referencedColumnName = "id")
		private User user;

		@ManyToOne(fetch = FetchType.EAGER)
		@JoinColumn(name = "order_status", referencedColumnName = "id")
		private OrderStatus orderStatus;

		public SaleOrder() {
			super();
		}
		public SaleOrder(Integer id, String code, BigDecimal total, String customerName, String customerMobile,
				String customerEmail, String customerAddress, LocalDate createDate, LocalDate updateDate,
				LocalDate deliveryDate, Boolean status, String description, List<SaleOrderProduct> saleOrderProducts,
				User user, OrderStatus orderStatus) {
			super();
			this.id = id;
			this.code = code;
			this.total = total;
			this.customerName = customerName;
			this.customerMobile = customerMobile;
			this.customerEmail = customerEmail;
			this.customerAddress = customerAddress;
			this.createDate = createDate;
			this.updateDate = updateDate;
			this.deliveryDate = deliveryDate;
			this.status = status;
			this.description = description;
			this.saleOrderProducts = saleOrderProducts;
			this.user = user;
			this.orderStatus = orderStatus;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public BigDecimal getTotal() {
			return total;
		}
		public void setTotal(BigDecimal total) {
			this.total = total;
		}
		public String getCustomerName() {
			return customerName;
		}
		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}
		public String getCustomerMobile() {
			return customerMobile;
		}
		public void setCustomerMobile(String customerMobile) {
			this.customerMobile = customerMobile;
		}
		public String getCustomerEmail() {
			return customerEmail;
		}
		public void setCustomerEmail(String customerEmail) {
			this.customerEmail = customerEmail;
		}
		public String getCustomerAddress() {
			return customerAddress;
		}
		public void setCustomerAddress(String customerAddress) {
			this.customerAddress = customerAddress;
		}
		public LocalDate getCreateDate() {
			return createDate;
		}
		public void setCreateDate(LocalDate createDate) {
			this.createDate = createDate;
		}
		public LocalDate getUpdateDate() {
			return updateDate;
		}
		public void setUpdateDate(LocalDate updateDate) {
			this.updateDate = updateDate;
		}
		public LocalDate getDeliveryDate() {
			return deliveryDate;
		}
		public void setDeliveryDate(LocalDate deliveryDate) {
			this.deliveryDate = deliveryDate;
		}
		public Boolean getStatus() {
			return status;
		}
		public void setStatus(Boolean status) {
			this.status = status;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public List<SaleOrderProduct> getSaleOrderProducts() {
			return saleOrderProducts;
		}
		public void setSaleOrderProducts(List<SaleOrderProduct> saleOrderProducts) {
			this.saleOrderProducts = saleOrderProducts;
		}
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
		public OrderStatus getOrderStatus() {
			return orderStatus;
		}
		public void setOrderStatus(OrderStatus orderStatus) {
			this.orderStatus = orderStatus;
		}
		

	    


}
