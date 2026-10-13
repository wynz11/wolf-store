package devpro.example.CommerceAdmin.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProductDto {

	@NotBlank(message = "Ten san pham khong duoc de trong")
	private String productName;

	@NotNull(message = "Giá sản phẩm không được để trống")
	@Min(value = 0, message = "Giá sản phẩm phải lớn hơn hoặc bằng 0")
	private BigDecimal price;

	@NotNull(message = "Vui lòng nhập giá gốc")
	@Positive(message = "Giá gốc phải lớn hơn 0")
	private BigDecimal oldPrice;

	@NotNull(message = "Vui lòng nhập phần trăm giảm giá")
	@Min(value = 0, message = "Giảm giá không được nhỏ hơn 0%")
	@Max(value = 100, message = "Giảm giá không được lớn hơn 100%")
	private Integer discountPercent;

	@NotNull(message = "Số lượng không được để trống")
	@Min(value = 0, message = "Số lượng phải lớn hơn hoặc bằng 0")
	private Integer quantity;
	
	private String shortDescription;

	private Integer categoryId;

	private Boolean status = Boolean.TRUE;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate createDate;

	public ProductDto() {
		super();
	}

	public ProductDto(@NotBlank(message = "Ten san pham khong duoc de trong") String productName,
			@NotNull(message = "Giá sản phẩm không được để trống") @Min(value = 0, message = "Giá sản phẩm phải lớn hơn hoặc bằng 0") BigDecimal price,
			@NotNull(message = "Vui lòng nhập giá gốc") @Positive(message = "Giá gốc phải lớn hơn 0") BigDecimal oldPrice,
			@NotNull(message = "Vui lòng nhập phần trăm giảm giá") @Min(value = 0, message = "Giảm giá không được nhỏ hơn 0%") @Max(value = 100, message = "Giảm giá không được lớn hơn 100%") Integer discountPercent,
			@NotNull(message = "Số lượng không được để trống") @Min(value = 0, message = "Số lượng phải lớn hơn hoặc bằng 0") Integer quantity,
			String shortDescription, Integer categoryId, Boolean status, LocalDate createDate) {
		super();
		this.productName = productName;
		this.price = price;
		this.oldPrice = oldPrice;
		this.discountPercent = discountPercent;
		this.quantity = quantity;
		this.shortDescription = shortDescription;
		this.categoryId = categoryId;
		this.status = status;
		this.createDate = createDate;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
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

	

	
	

	
}
