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

/**
 * 
 */
@Entity
@Table(name = "tbl_product")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@Column(name = "avatar")
	private String avatar;

	@Column(name = "name")
	private String name;

	@Column(name = "price")
	private BigDecimal price;

	@Column(name = "old_price")
	private BigDecimal oldPrice;

	@Column(name = "discount_percent")
	private Integer discountPercent;

	@Column(name = "quantity")
	private Integer quantity; 
	
	@Column(name = "short_description")
	private String shortDescription;

	@Column(name = "status")
	private Boolean status = true;

	@Column(name = "create_date")
	private LocalDate createDate;


	// Quan hệ 1 Product - nhiều ProductImage
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ProductImage> productImages = new ArrayList<>();

	public void addRelationalProductImage(ProductImage image) {
		productImages.add(image);
		image.setProduct(this);
	}

	public void removeRelationalProductImage(ProductImage image) {
		productImages.remove(image);
		image.setProduct(this);
	}

	public Product() {
		super();
	}

	public Product(Integer id, Category category, String avatar, String name, BigDecimal price, BigDecimal oldPrice,
			Integer discountPercent, Integer quantity, String shortDescription, Boolean status, LocalDate createDate,
			List<ProductImage> productImages) {
		super();
		this.id = id;
		this.category = category;
		this.avatar = avatar;
		this.name = name;
		this.price = price;
		this.oldPrice = oldPrice;
		this.discountPercent = discountPercent;
		this.quantity = quantity;
		this.shortDescription = shortDescription;
		this.status = status;
		this.createDate = createDate;
		this.productImages = productImages;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(BigDecimal oldPrice) {
		this.oldPrice = oldPrice;
	}

	public Integer getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(Integer discountPercent) {
		this.discountPercent = discountPercent;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	

	

}
