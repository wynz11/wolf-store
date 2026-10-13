package devpro.example.CommerceAdmin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import devpro.example.CommerceAdmin.model.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer>,
												JpaSpecificationExecutor<ProductImage>{
	
	List<ProductImage> findAllByProductId(Integer productId);
	

}
