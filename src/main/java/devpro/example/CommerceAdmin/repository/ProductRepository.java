package devpro.example.CommerceAdmin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import devpro.example.CommerceAdmin.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {

	long countByCategoryId(Integer categoryId);

	public Boolean existsByName(String name);

	Optional<Product> findById(Integer id);

	public List<Product> findByStatusTrue();
	
	List<Product> findTop10ByStatusTrueOrderByIdDesc();

	// sản phẩm mới nhất
	List<Product> findTop8ByIdNotInOrderByIdDesc(List<Integer> ids);
	
	List<Product> findTop4ByIdNotOrderByIdDesc(Integer productId);
	
	List<Product> findTop4ByOrderByIdDesc();

	// laptop giá cao nhất
	Product findTopByCategory_NameOrderByPriceDesc(String categoryName);

	List<Product> findTop4ByCategoryIdAndIdNot(Integer categoryId, Integer id);

}
