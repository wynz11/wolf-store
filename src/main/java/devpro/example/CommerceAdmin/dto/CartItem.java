package devpro.example.CommerceAdmin.dto;

import java.math.BigDecimal;

public class CartItem {

	private Integer id;
	private String name;
	private int quantity;
	private BigDecimal price;
	private String avatar;

	// Phuong thuc tinh thanh tien = so luong * don gia
	public BigDecimal getTotalPrice() {
		if (this.price == null) {
			return BigDecimal.ZERO;
		}

		return this.price.multiply(BigDecimal.valueOf(this.quantity));
	}

	// Phuong thuc tang/giam them so luong cua san pham
	public void updateQuantity(int quantity) {
		this.quantity = this.quantity + quantity;
	}

	public CartItem() {
		super();
	}

	public CartItem(Integer id, String name, int quantity, BigDecimal price, String avatar) {
		super();
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.avatar = avatar;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}
