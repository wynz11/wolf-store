package devpro.example.CommerceAdmin.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import devpro.example.CommerceAdmin.dto.CategoryDto;
import devpro.example.CommerceAdmin.dto.J34Contant;
import devpro.example.CommerceAdmin.model.Category;
import devpro.example.CommerceAdmin.repository.CategoryRepository;
import devpro.example.CommerceAdmin.repository.ProductRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

@Service
public class CategoryService implements J34Contant {
	
	private final CategoryRepository crp;
	private final ProductRepository prp;

	public CategoryService(CategoryRepository crp, ProductRepository prp) {
		this.crp = crp;
		this.prp = prp;
	}
	
	public List<Category> findAll(){
		 List<Category> categories = crp.findAll();

		    for (Category category : categories) {
		        long count = prp.countByCategoryId(category.getId());
		        category.setProductCount(count);
		    }

		    return categories;
	}
	
	public Boolean existsByName(String name) {
		return crp.existsByName(name);
	}
	
	public Category findById(Integer id) {
		return crp.findById(id).orElse(null);
	}
	
	public Boolean existsByNameExceptId(String name, Integer id) {
		
		List<Category> categories = crp.findAll();
		for(Category category : categories) {
			if(category.getId() != id &&
					category.getName().trim().equalsIgnoreCase(name.trim())) {
				return true;
			}
		}
		return false;
	}
	
	
	public long countAll() {
	    return crp.count();
	}

	public long countActive() {
	    return crp.countByStatus(true);
	}

	public long countInactive() {
	    return crp.countByStatus(false);
	}
	
	
	
	public Page<Category> search(
			String keyword,
			Boolean status, 
			Pageable pageable) {

		Specification<Category> spec =   (root, query, cb) -> null;

		if (keyword != null && !keyword.isEmpty()) {

			String kw = "%" + keyword.trim().toLowerCase() + "%";
			spec = spec.and((root, q, cb) -> {

				q.distinct(true);
				List<Predicate> ors = new ArrayList<>(); // Danh sach ket hop dieu kien
				ors.add(cb.like(cb.lower(root.get("name")), kw));
				ors.add(cb.like(cb.lower(root.get("description")), kw));
				return cb.or(ors.toArray(new Predicate[0]));

			});
		}

		// Status
		if (status != null) {
			spec = spec.and((root, q, cb) -> cb.equal(root.get("status"), status));
		}


		return crp.findAll(spec, pageable);
	}

	
	public Boolean isExistFile(MultipartFile file) {
		if(file != null && file.getOriginalFilename() != null &&
			file.getOriginalFilename().length() > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	
	
	public Category insert(CategoryDto categoryDto,
			MultipartFile avatarFile) throws IOException {
		
		Category category = new Category();
		category.setId(null);
		
		category.setName(categoryDto.getName());
		category.setDescription(categoryDto.getDescription());
		category.setStatus(categoryDto.getStatus() != null ?
				categoryDto.getStatus() : Boolean.TRUE);
		
		 if (avatarFile != null && !avatarFile.isEmpty()) {
//			 
			 String fileName = avatarFile.getOriginalFilename();

		        category.setAvatar(
		                "Category/Avatar/" +
		                avatarFile.getOriginalFilename());

		        String path = FOLDER_PATH
		                + "/Category/Avatar/"
		                + avatarFile.getOriginalFilename();

		        File file = new File(path);

		        file.getParentFile().mkdirs();

		        avatarFile.transferTo(file);
		       
		    }else {
		    	category.setAvatar(null);
		    }
		 
		 return crp.save(category);
		
		
	}
	
	public Category update(CategoryDto categoryDto , Integer id,
			MultipartFile avatarFile) throws IOException{
		
		
		Category category = crp.findById(id).orElse(null);
		
		if(category == null) {
			return category;
		}
		
		category.setName(categoryDto.getName());
		category.setDescription(categoryDto.getDescription());
		category.setStatus(categoryDto.getStatus() != null ?
				categoryDto.getStatus() : Boolean.TRUE);
		
		if(isExistFile(avatarFile)) {
			
			if(category.getAvatar() != null) {
				String path = FOLDER_PATH + category.getAvatar();
				File file = new File(path);
				file.delete();
			}
			
			category.setAvatar("Category/Avatar/" + avatarFile.getOriginalFilename()) ;
			
			String path = FOLDER_PATH + "Category/Avatar/" +
									avatarFile.getOriginalFilename();
			File file = new File(path);
			avatarFile.transferTo(file);
		}
		 
		 return crp.save(category);
		
	}
	
	//Inactive
		public void inactive(Integer id) {
			// Lay du lieu trong DB
			Category category = findById(id);
			if(category == null) {
				return ;
			}
			category.setStatus(!category.getStatus());
			crp.save(category);
			
			//Xoa hẳn
//			 crp.delete(category);
		}


}
