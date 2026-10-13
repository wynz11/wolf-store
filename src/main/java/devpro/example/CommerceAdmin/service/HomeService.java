package devpro.example.CommerceAdmin.service;

import org.springframework.stereotype.Service;

import devpro.example.CommerceAdmin.dto.J34Contant;
import devpro.example.CommerceAdmin.model.Product;
import devpro.example.CommerceAdmin.repository.ProductRepository;

@Service
public class HomeService implements J34Contant {
	
	private final ProductRepository prp;

	public HomeService(ProductRepository prp) {
		this.prp = prp;
	}
	
	  public Product getHeroProduct() {
	        return prp.findById(1).orElse(null);
	    }

	    public Product getComboProduct() {
	        return prp.findById(2).orElse(null);
	    }

	    public Product getGamingProduct() {
	        return prp.findById(3).orElse(null);
	    }

	    public Product getAccessoryProduct() {
	        return prp.findById(4).orElse(null);
	    }
	
	

}
