package devpro.example.CommerceAdmin.controller.administrator;

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

import devpro.example.CommerceAdmin.dto.CategoryDto;
import devpro.example.CommerceAdmin.model.Category;
import devpro.example.CommerceAdmin.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {

	@Autowired
	private CategoryService cs;

	@GetMapping({ "/", "" })
	public String home(final Model model,
			HttpServletRequest request,
			
			
			//Phân trang 
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size,
			
			//Sap xep
			@RequestParam(defaultValue = "name") String column,
			@RequestParam(defaultValue = "asc") String direction,
			
			//Tìm kiếm 
			@RequestParam(required = false)  String keyword,
			@RequestParam(required = false)  String status	) {
		
		
		Boolean statusBool = null; // Khon chon  = all
		if("true".equalsIgnoreCase(status))
			statusBool = Boolean.TRUE;
		if("false".equalsIgnoreCase(status))
			statusBool = Boolean.FALSE;
		
		
		//Sap xep
				Sort.Direction dir = direction.equalsIgnoreCase("asc") ?
						Sort.Direction.ASC : Sort.Direction.DESC;
				
		//Phan trang
			PageRequest pageable =
				PageRequest.of(page, size, Sort.by(dir, column));
				
			Page<Category> categoriesPage = cs.search(
					keyword,
					statusBool,
				    pageable);	
				
				
		
		
		

		List<Category> categories = cs.findAll();

		// Tổng số danh mục
		int totalCategory = categories.size();

		// Đang hoạt động
		int activeCategory = 0;

		// Tạm khóa
		int inactiveCategory = 0;

		for (Category category : categories) {

			if (Boolean.TRUE.equals(category.getStatus())) {
				activeCategory++;
			} else {
				inactiveCategory++;
			}
		}

		model.addAttribute("categories", categories);
		
		 model.addAttribute("categoriesPage", categoriesPage);

		    model.addAttribute("currentPage", page);

		    model.addAttribute("pageSize", size);

		    model.addAttribute("column", column);

		    model.addAttribute("direction", direction);

		    model.addAttribute("keyword", keyword);

		    model.addAttribute("status", status);
		
		model.addAttribute("totalCategory", totalCategory);
		model.addAttribute("activeCategory", activeCategory);
		model.addAttribute("inactiveCategory", inactiveCategory);

		return "administrator/category/category";
	}

	@GetMapping("/add")
	public String add(final Model model) {

		CategoryDto categoryDto = new CategoryDto();

		model.addAttribute("categoryDto", categoryDto);

		List<Category> categories = cs.findAll();
		model.addAttribute("categories", categories);

		return "administrator/category/add";

	}

	@PostMapping("/save")
	public String saveNewCategory(Model model, @Valid @ModelAttribute CategoryDto categoryDto, BindingResult result,
			@RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile) {

		if (cs.existsByName(categoryDto.getName())) {
			result.reject("name", null, "The name is already used");
		}

		if (result.hasErrors()) {

			List<Category> categories = cs.findAll();
			model.addAttribute("categories", categories);
			model.addAttribute("categoryDto", categoryDto);

			return "administrator/category/add";

		}

		try {
			cs.insert(categoryDto, avatarFile);
		} catch (Exception e) {
			e.printStackTrace();

			List<Category> categories = cs.findAll();
			model.addAttribute("categories", categories);
			model.addAttribute("categoryDto", categoryDto);

			return "administrator/category/add";

		}

		return "redirect:/admin/category";

	}

	@GetMapping("/edit")
	public String editCategory(final Model model, @RequestParam int id) {

		Category category = cs.findById(id);

		if (category == null) {
			return "redirect:/admin/category";
		}

		CategoryDto categoryDto = new CategoryDto();

		categoryDto.setCategoryId(category.getParentCategory() != null ? category.getParentCategory().getId() : null);

		categoryDto.setName(category.getName());
		categoryDto.setDescription(category.getDescription());
		categoryDto.setStatus(category.getStatus());

		model.addAttribute("categoryDto", categoryDto);
		model.addAttribute("category", category);
		List<Category> categories = cs.findAll();
		model.addAttribute("categories", categories);

		return "administrator/category/edit";

	}

	@PostMapping("/update")
	public String saveEditCategory(Model model, @Valid @ModelAttribute CategoryDto categoryDto, BindingResult result,
			@RequestParam Integer id, @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile) {

		if (id == null || id < 1) {
			return "redirect:/admin/category";
		}

		Category category = new Category();
		category.setId(id);

		if (cs.existsByNameExceptId(categoryDto.getName(), id)) {
			result.reject("name", null, "Ten danh mục da su dung");
		}

		if (result.hasErrors()) {
			List<Category> categories = cs.findAll();

			model.addAttribute("categories", categories);
			model.addAttribute("categoryDto", categoryDto);
			model.addAttribute("category", category);

			return "administrator/category/edit";
		}

		try {
			cs.update(categoryDto, id, avatarFile);
		} catch (Exception e) {
			e.printStackTrace();

			List<Category> categories = cs.findAll();
			model.addAttribute("categories", categories);
			model.addAttribute("categoryDto", categoryDto);
			model.addAttribute("category", category);

			return "administrator/category/edit";
		}

		return "redirect:/admin/category";
	}

	@GetMapping("/delete")
	public String deleteCategory(@RequestParam Integer id) {

		if (id != null && id > 0) {
			cs.inactive(id);
		}

		return "redirect:/admin/category";

	}

}
