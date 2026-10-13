package devpro.example.CommerceAdmin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import devpro.example.CommerceAdmin.model.Category;
import devpro.example.CommerceAdmin.model.Product;
import devpro.example.CommerceAdmin.model.ProductImage;
import devpro.example.CommerceAdmin.service.CategoryService;
import devpro.example.CommerceAdmin.service.HomeService;
import devpro.example.CommerceAdmin.service.ProductImageService;
import devpro.example.CommerceAdmin.service.ProductService;

@Controller
@RequestMapping("/home")
public class CustomerHomeController extends BaseController {

	@Autowired
	private HomeService hs;
	
	@Autowired
	private CategoryService cs;

	@Autowired
	private ProductService ps;

	@Autowired
	private ProductImageService pis;

	@GetMapping({ "/", "" })
	public String home(final Model model) {

		List<Product> flashSaleProducts = ps.getFlashSaleProducts();
		
		List<Category> categories = cs.findAll();
		
		model.addAttribute("categories", categories);
		
		  model.addAttribute(
	                "featuredProducts",
	                ps.getFeaturedProducts());

		model.addAttribute("flashSaleProducts", flashSaleProducts);

		model.addAttribute("heroProduct", hs.getHeroProduct());

		model.addAttribute("comboProduct", hs.getComboProduct());

		model.addAttribute("gamingProduct", hs.getGamingProduct());

		model.addAttribute("accessoryProduct", hs.getAccessoryProduct());

		return "storefront/home/index";
	}

	@GetMapping("/product")
	public String productDetail(Model model, @RequestParam(name = "id") Integer id) {

		Product product = ps.findById(id);

		if (product == null) {
			return "redirect:/home";
		}

		List<ProductImage> productImages = pis.findAllByProductId(product.getId());

		List<Product> relatedProducts =
		        ps.getRelatedProducts(product.getId());

		model.addAttribute("productImages", productImages);
		model.addAttribute("product", product);
		model.addAttribute("relatedProducts", relatedProducts);

		return "storefront/home/product";
	}

}
