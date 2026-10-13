package devpro.example.CommerceAdmin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import devpro.example.CommerceAdmin.model.ProductImage;
import devpro.example.CommerceAdmin.repository.ProductImageRepository;

@Service
public class ProductImageService {
	
	private final ProductImageRepository pir;

	public ProductImageService(ProductImageRepository pir) {
		super();
		this.pir = pir;
	}
	
	public List<ProductImage> findAllByProductId(Integer productId){
		return pir.findAllByProductId(productId);
	}
	
	

}
