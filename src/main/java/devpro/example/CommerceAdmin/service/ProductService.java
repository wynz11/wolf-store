package devpro.example.CommerceAdmin.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import devpro.example.CommerceAdmin.dto.J34Contant;
import devpro.example.CommerceAdmin.dto.ProductDto;
import devpro.example.CommerceAdmin.model.Category;
import devpro.example.CommerceAdmin.model.Product;
import devpro.example.CommerceAdmin.model.ProductImage;
import devpro.example.CommerceAdmin.repository.CategoryRepository;
import devpro.example.CommerceAdmin.repository.ProductRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

@Service
public class ProductService implements J34Contant {

	private final ProductRepository prp;
	private final CategoryRepository crp;

	public ProductService(ProductRepository prp, CategoryRepository crp) {
		super();
		this.prp = prp;
		this.crp = crp;
	}

	public List<Product> findAll() {
		return prp.findAll();
	}

	public Boolean existsByName(String name) {
		return prp.existsByName(name);
	}

	public Product findById(Integer id) {
		return prp.findById(id).orElse(null);
	}

	public List<Product> getFlashSaleProducts() {
		return prp.findTop8ByIdNotInOrderByIdDesc(Arrays.asList(1, 2, 3, 4));
	}

	public List<Product> getRelatedProducts(Integer productId) {
	    return prp.findTop4ByIdNotOrderByIdDesc(productId);
	}
	
	public List<Product> getSuggestedProducts() {
	    return prp.findTop4ByOrderByIdDesc();
	}
	 public List<Product> getFeaturedProducts() {
	        return prp.findTop10ByStatusTrueOrderByIdDesc();
	    }

	public Boolean existsByNameExceptId(String name, Integer id) {

		List<Product> products = prp.findAll();
		for (Product product : products) {
			if (product.getId() != id && product.getName().trim().equalsIgnoreCase(name.trim())) {
				return true;
			}
		}
		return false;
	}

	public Page<Product> search(String keyword, Integer categoryId, Boolean status, Pageable pageable) {

		Specification<Product> spec = (root, query, cb) -> null;

		if (keyword != null && !keyword.isEmpty()) {

			String kw = "%" + keyword.trim().toLowerCase() + "%";
			spec = spec.and((root, q, cb) -> {

				q.distinct(true);
				List<Predicate> ors = new ArrayList<>(); // Danh sach ket hop dieu kien
				ors.add(cb.like(cb.lower(root.get("name")), kw));
				Join<Product, Category> cat = root.join("category", JoinType.LEFT);
				ors.add(cb.like(cb.lower(cat.get("name")), kw));
				return cb.or(ors.toArray(new Predicate[0]));

			});
		}

		// Category
		if (categoryId != null && categoryId != 0) {
			spec = spec.and((root, q, cb) -> cb.equal(root.get("category").get("id"), categoryId));
		}

		// Status
		if (status != null) {
			spec = spec.and((root, q, cb) -> cb.equal(root.get("status"), status));
		}

		return prp.findAll(spec, pageable);
	}

	public Boolean isExistFile(MultipartFile file) {
		if (file != null && file.getOriginalFilename() != null && file.getOriginalFilename().length() > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean isExistFiles(MultipartFile[] files) {
		if (files != null && files.length > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Product insert(ProductDto productDto, MultipartFile avatarFile, MultipartFile[] imageFiles)
			throws IOException {

		Product product = new Product();

		product.setId(null);

		Category category = crp.findById(productDto.getCategoryId()).orElse(null);

		product.setCategory(category);

		product.setName(productDto.getProductName());
		product.setPrice(productDto.getPrice());
		product.setOldPrice(productDto.getOldPrice());
		product.setShortDescription(productDto.getShortDescription());
		;
		product.setDiscountPercent(productDto.getDiscountPercent());
		product.setQuantity(productDto.getQuantity());
		product.setStatus(productDto.getStatus());
		product.setCreateDate(productDto.getCreateDate());

		if (productDto.getPrice() != null) {
			product.setPrice(productDto.getPrice());
		} else {
			product.setPrice(BigDecimal.ZERO);
		}

		if (isExistFile(avatarFile)) {
			product.setAvatar("Product/Avatar/" + avatarFile.getOriginalFilename());

			String path = FOLDER_PATH + "Product/Avatar/" + avatarFile.getOriginalFilename();
			File file = new File(path);
			avatarFile.transferTo(file);
		} else {
			product.setAvatar(null);
		}

		if (isExistFiles(imageFiles)) {
			for (MultipartFile imageFile : imageFiles) {
				if (isExistFiles(imageFiles)) {
					String path = FOLDER_PATH + "Product/Image/" + imageFile.getOriginalFilename();
					File file = new File(path);
					imageFile.transferTo(file);

					ProductImage productImage = new ProductImage();
					productImage.setId(null);
					productImage.setTitle(imageFile.getOriginalFilename());
					// Luu duong dan db
					productImage.setPath("Product/Image/" + imageFile.getOriginalFilename());
					productImage.setStatus(Boolean.TRUE);

					productImage.setProduct(product);

					product.addRelationalProductImage(productImage);
				}
			}
		}

		return prp.save(product);

	}

	public Product update(ProductDto productDto, Integer id, MultipartFile avatarFile, MultipartFile[] imageFiles)
			throws IOException {

		Product product = prp.findById(id).orElse(null);

		if (product == null) {
			return product;
		}

		if (productDto.getCategoryId() != null && productDto.getCategoryId() != 0) {
			Category category = crp.findById(productDto.getCategoryId()).orElse(null);

			product.setCategory(category);
		} else {
			product.setCategory(null);
		}

		product.setName(productDto.getProductName());
		product.setPrice(productDto.getPrice());
		product.setOldPrice(productDto.getOldPrice());
		product.setDiscountPercent(productDto.getDiscountPercent());
		product.setShortDescription(productDto.getShortDescription());
		product.setQuantity(productDto.getQuantity());
		product.setStatus(productDto.getStatus() != null ? productDto.getStatus() : Boolean.TRUE);
		product.setCreateDate(productDto.getCreateDate());

		if (productDto.getPrice() != null) {
			product.setPrice(productDto.getPrice());
		} else {
			product.setPrice(BigDecimal.ZERO);
		}

		if (isExistFile(avatarFile)) {

			if (product.getAvatar() != null) {
				String path = FOLDER_PATH + product.getAvatar();
				File file = new File(path);
				file.delete();
			}

			product.setAvatar("Product/Avatar/" + avatarFile.getOriginalFilename());

			String path = FOLDER_PATH + "Product/Avatar/" + avatarFile.getOriginalFilename();
			File file = new File(path);
			avatarFile.transferTo(file);
		}
		if (isExistFiles(imageFiles)) {
			for (MultipartFile imageFile : imageFiles) {
				if (isExistFiles(imageFiles)) {

					String path = FOLDER_PATH + "Product/Image/" + imageFile.getOriginalFilename();
					File file = new File(path);
					imageFile.transferTo(file);

					ProductImage productImage = new ProductImage();
					productImage.setId(null);
					productImage.setTitle(imageFile.getOriginalFilename());
					// Luu duong dan db
					productImage.setPath("Product/Image/" + imageFile.getOriginalFilename());
					productImage.setStatus(Boolean.TRUE);

					productImage.setProduct(product);

					product.addRelationalProductImage(productImage);
				}
			}
		}

		return prp.save(product);

	}

	public void inactive(Integer id) {

		Product product = findById(id);

		if (product == null) {
			return;
		}

		product.setStatus(!product.getStatus());
		prp.save(product);

		// Xoa hẳn
//		 prp.delete(product);
	}

}
