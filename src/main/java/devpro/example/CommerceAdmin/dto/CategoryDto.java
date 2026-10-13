package devpro.example.CommerceAdmin.dto;

import jakarta.validation.constraints.NotEmpty;

public class CategoryDto {
	
	@NotEmpty(message = "The name is required")
	private String name;
	
	@NotEmpty(message = "Mô tả không được để trống")
	private String description;
	
	private Boolean status = Boolean.TRUE;

	
	private Integer categoryId;

	public CategoryDto() {
		super();
	}

	public CategoryDto(@NotEmpty(message = "The name is required") String name,
			@NotEmpty(message = "Mô tả không được để trống") String description, Boolean status,
			Integer categoryId) {
		super();
		this.name = name;
		this.description = description;
		this.status = status;
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}


	
}
