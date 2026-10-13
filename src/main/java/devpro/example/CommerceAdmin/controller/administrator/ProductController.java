package devpro.example.CommerceAdmin.controller.administrator;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import devpro.example.CommerceAdmin.dto.ProductDto;
import devpro.example.CommerceAdmin.model.Category;
import devpro.example.CommerceAdmin.model.Product;
import devpro.example.CommerceAdmin.service.CategoryService;
import devpro.example.CommerceAdmin.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/product")
public class ProductController {

	@Autowired
	private ProductService ps;

	@Autowired
	private CategoryService cs;

	@GetMapping({ "/", "" })
	public String home(Model model, HttpServletRequest request,

			// Phân trang
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size,

			// Sap xep
			@RequestParam(defaultValue = "name") String column, @RequestParam(defaultValue = "asc") String direction,

			// Tìm kiếm
			@RequestParam(required = false) String keyword, @RequestParam(required = false) String status,
			@RequestParam(required = false) Integer categoryId) {

		Boolean statusBool = null;
		if ("true".equalsIgnoreCase(status))
			statusBool = Boolean.TRUE;
		if ("false".equalsIgnoreCase(status))
			statusBool = Boolean.FALSE;

		// Sap xep
		Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

//Phan trang
		PageRequest pageable =
				PageRequest.of(page, size, Sort.by(dir, column));
		
		Page<Product> productsPage = ps.search(
				keyword,
				categoryId == null ? 0 : categoryId,
				statusBool,
			    pageable);	
		
		List<Category> categories = cs.findAll();
		
		model.addAttribute("products", productsPage.getContent());
		model.addAttribute("productsPage", productsPage);
		
		 model.addAttribute("currentPage", page);
		    model.addAttribute("pageSize", size);
		    model.addAttribute("column", column);
		    model.addAttribute("direction", direction);

		    model.addAttribute("categories", categories);
		    model.addAttribute("categoryId", categoryId);
		    model.addAttribute("keyword", keyword);
		    model.addAttribute("status", status);

		return "administrator/product/product";
	}

	@GetMapping("/add")
	public String addNewProduct(final Model model) {

		ProductDto productDto = new ProductDto();

		model.addAttribute("productDto", productDto);
		productDto.setCreateDate(LocalDate.now());
		List<Category> categories = cs.findAll();
		model.addAttribute("categories", categories);

		return "administrator/product/add";

	}

	@PostMapping("/save")
	public String saveNewProduct(Model model, @Valid @ModelAttribute ProductDto productDto, BindingResult result,
			@RequestParam MultipartFile avatarFile, @RequestParam MultipartFile[] imageFiles) {

		if (productDto.getCategoryId() == null || productDto.getCategoryId() == 0) {
			result.rejectValue("categoryId", null, "Chưa chọn danh muc cho sản phẩm");
		}

		if (ps.existsByName(productDto.getProductName())) {
			result.rejectValue("productName", null, "Ten san pham da su dung");
		}

		if (productDto.getCreateDate() == null) {
			result.rejectValue("createDate", null, "Chua chon ngay tao san pham");
		}

		if (result.hasErrors()) {

			model.addAttribute("productDto", productDto);

			List<Category> categories = cs.findAll();
			productDto.setCreateDate(LocalDate.now());
			model.addAttribute("categories", categories);

			return "administrator/product/add";
		}

		try {
			ps.insert(productDto, avatarFile, imageFiles);
		} catch (Exception e) {
			e.printStackTrace();

			model.addAttribute("productDto", productDto);

			List<Category> categories = cs.findAll();
			productDto.setCreateDate(LocalDate.now());
			model.addAttribute("categories", categories);

			return "administrator/product/add";

		}

		return "redirect:/admin/product";

	}

	@GetMapping("/edit")
	public String editProduct(final Model model, @RequestParam int id) {

		Product product = ps.findById(id);

		if (product == null) {
			return "redirect:/admin/product";
		}

		ProductDto productDto = new ProductDto();

		productDto.setProductName(product.getName());
		productDto.setCreateDate(product.getCreateDate());
		productDto.setQuantity(product.getQuantity());
		productDto.setShortDescription(product.getShortDescription());
		productDto.setPrice(product.getPrice());
		productDto.setOldPrice(product.getOldPrice());
		productDto.setDiscountPercent(product.getDiscountPercent());
		productDto.setStatus(product.getStatus());

		// Lấy category hiện tại của Product
		if (product.getCategory() != null) {
			productDto.setCategoryId(product.getCategory().getId());
		}

		model.addAttribute("productDto", productDto);
		model.addAttribute("product", product);

		List<Category> categories = cs.findAll();
		model.addAttribute("categories", categories);

		return "administrator/product/edit";
	}

	@PostMapping("/update")
	public String updateEditProduct(Model model, @Valid @ModelAttribute ProductDto productDto, BindingResult result,
			@RequestParam(name = "id", required = false) Integer id,
			@RequestParam(required = false) MultipartFile avatarFile,
			@RequestParam(required = false) MultipartFile[] imageFiles) {

		if (id == null || id < 1) {
			return "redirect:/admin/product";
		}

		Product product = ps.findById(id);

		if (product == null) {
			return "redirect:/admin/product";
		}

//		Product product = new Product();
//		product.setId(id);

		if (productDto.getCategoryId() == null || productDto.getCategoryId() == 0) {
			result.rejectValue("categoryId", null, "Chua chon danh muc cho san pham");
		}

		if (ps.existsByNameExceptId(productDto.getProductName(), id)) {
			result.rejectValue("productName", null, "Ten san pham da su dung");
		}

		if (productDto.getCreateDate() == null) {
			result.rejectValue("createDate", null, "Chua chon ngay tao san pham");
		}

		if (result.hasErrors()) {
			model.addAttribute("productDto", productDto);
			model.addAttribute("product", product);

			List<Category> categories = cs.findAll();
			model.addAttribute("categories", categories);

			return "administrator/product/edit";
		}

		try {
			ps.update(productDto, id, avatarFile, imageFiles);
		} catch (Exception e) {
			e.printStackTrace();

			model.addAttribute("productDto", productDto);
			model.addAttribute("product", product);

			List<Category> categories = cs.findAll();
			model.addAttribute("categories", categories);

			return "administrator/product/edit";

		}

		return "redirect:/admin/product";

	}

	@GetMapping("/delete")
	public String deleteProduct(@RequestParam Integer id) {

		if (id != null && id > 0) {
			ps.inactive(id);
		}

		return "redirect:/admin/product";
	}

}
