package devpro.example.CommerceAdmin.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import devpro.example.CommerceAdmin.dto.Cart;
import devpro.example.CommerceAdmin.dto.CartItem;
import devpro.example.CommerceAdmin.model.OrderStatus;
import devpro.example.CommerceAdmin.model.Product;
import devpro.example.CommerceAdmin.model.SaleOrder;
import devpro.example.CommerceAdmin.model.SaleOrderProduct;
import devpro.example.CommerceAdmin.model.User;
import devpro.example.CommerceAdmin.service.OrderStatusSerVice;
import devpro.example.CommerceAdmin.service.ProductService;
import devpro.example.CommerceAdmin.service.SaleOrderService;
import devpro.example.CommerceAdmin.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class CartController extends BaseController {

	@Autowired
	private ProductService ps;

	@Autowired
	private UserService us;

	@Autowired
	private OrderStatusSerVice os;

	@Autowired
	private SaleOrderService ss;

//	
	@PostMapping("/cart/add")
	@ResponseBody
	public Map<String, Object> addToCart(@RequestParam Integer productId,
			@RequestParam(defaultValue = "1") Integer addQuantity, @RequestParam String name, HttpSession session) {
		Cart cart = getCart(session);

		Product p = ps.findById(productId);// Lay san pham trong db

		int index = cart.findById(productId);// Tim xem sp co trong gio hay chua?

		if (index == -1) { // San pham chua co trong gio, them moi
			CartItem item = new CartItem(p.getId(), p.getName(), addQuantity,
					p.getPrice().doubleValue() > 0 ? p.getPrice() : p.getOldPrice(), p.getAvatar());
			cart.getItems().add(item);// Them sp moi vao gio hang
		} else {// Sp da co trong gio => Tang so luong
			int oldQuantity = cart.getItems().get(index).getQuantity();
			cart.getItems().get(index).setQuantity(oldQuantity + addQuantity);
		}

		Map<String, Object> res = new HashMap<String, Object>();
		res.put("success", true);
		res.put("totalProducts", cart.getTotalProducts());
		res.put("totalCartPrice", cart.getTotalCartPrice());
		return res;
	}

	@GetMapping("/cart")
	public String cart(HttpSession session, Model model) {

		Cart cart = getCart(session);

//		User loginedUser = us.findById(7);
//		model.addAttribute("loginedUser", loginedUser);

		model.addAttribute("cart", cart);
		model.addAttribute("cartItems", cart.getItems());

		// Tong tien hang
		model.addAttribute("subtotal", cart.getTotalCartPrice());

		// Tong thanh toan
		model.addAttribute("grandTotal", cart.getGrandTotal());

		// Tong so luong san pham
		model.addAttribute("totalProduct", cart.getTotalProducts());

		// San pham goi y
		model.addAttribute("relatedProducts", ps.getSuggestedProducts());
		return "storefront/home/cart";
	}

	@PostMapping("/cart/remove")
	@ResponseBody
	public Map<String, Object> removeFromCart(@RequestParam Integer productId, HttpSession session) {

		Cart cart = getCart(session);
		cart.removeItem(productId);

		Map<String, Object> result = new HashMap<>();
		result.put("success", true);
		result.put("totalProducts", cart.getTotalProducts());
		result.put("subtotal", cart.getTotalCartPrice());
		result.put("grandTotal", cart.getGrandTotal());

		return result;
	}

	@GetMapping("/checkout")
	public String checkout(HttpSession session, Model model) {
		Cart cart = getCart(session);

		if (cart.getItems().isEmpty()) {
			return "redirect:/cart";
		}

		User loginedUser = us.findById(3);

		model.addAttribute("loginedUser", loginedUser);
		model.addAttribute("cart", cart);
		model.addAttribute("cartItems", cart.getItems());
		model.addAttribute("subtotal", cart.getTotalCartPrice());
		model.addAttribute("grandTotal", cart.getGrandTotal());
		model.addAttribute("totalProduct", cart.getTotalProducts());

		return "storefront/home/checkout";
	}

	@PostMapping("/cart/place-order")
	@ResponseBody
	public Map<String, Object> placeOrder(@RequestParam String name, @RequestParam String mobile,
			@RequestParam String address,@RequestParam(defaultValue = "") String description, HttpSession session) {
		Map<String, Object> res = new HashMap<String, Object>();

		if (name == null || name.trim().isEmpty()) {
			res.put("success", false);
			res.put("message", "Ban chua nhap ten");
			return res;
		}

		if (mobile == null || mobile.trim().length() < 10) {
			res.put("success", false);
			res.put("message", "Ban chua nhap dung so dien thoai");
			return res;
		}

		// Lay gio hang
		Cart cart = getCart(session);

		if (cart == null) {
			res.put("success", false);
			res.put("message", "Gio hang rong, khong the dat");
			return res;
		}

		if (cart.getItems() == null && cart.getItems().size() <= 0) {
			res.put("success", false);
			res.put("message", "Gio hang rong, khong the dat");
			return res;
		}

		// Gio hang san sang -> Tao va luu don hang
		SaleOrder saleOrder = new SaleOrder();

		String code = mobile + "_" + new Date().getTime();

		saleOrder.setId(null);
		saleOrder.setCode(code); // SỬA DÒNG NÀY
		saleOrder.setCreateDate(LocalDate.now());
		saleOrder.setCustomerName(name);
		saleOrder.setCustomerMobile(mobile);
		saleOrder.setCustomerAddress(address);
		saleOrder.setStatus(Boolean.TRUE);
		saleOrder.setTotal(cart.getTotalCartPrice());
		saleOrder.setDescription(description);

		OrderStatus orderStatus = os.findByName("Đơn hàng mới/Chờ xử lý");

		if (orderStatus == null) {
			res.put("success", false);
			res.put("message", "Co loi khi tao don hang");
			return res;
		}
		saleOrder.setOrderStatus(orderStatus);

		User user = getLoginedUser();
//		User user = us.findById(7);

//		if(user == null) {
//				user = us.findByUserName("customer");
//	}

		if (user == null) {
			user = us.findByUserName("customer");
		}

		saleOrder.setUser(user);

		// Luu cac sp trong gio hang vao tbl_sale_order_product
		for (CartItem cartItem : cart.getItems()) {

			SaleOrderProduct product = new SaleOrderProduct();
			product.setId(null);
			product.setName(cartItem.getName());
			product.setPrice(cartItem.getPrice());
			product.setQuantity(cartItem.getQuantity());
			product.setCreateDate(LocalDate.now());
			product.setStatus(Boolean.TRUE);

			// Lay san pham trong DB
			Product dbProduct = ps.findById(cartItem.getId());
			if (dbProduct == null) {
				res.put("success", false);
				res.put("message", "Co loi khi tao don hang");
				return res;
			}
			// San pham duoc ban la sp nao
			product.setProduct(dbProduct);
			// San pham duoc ban hang trong hoa don nao
			product.setSaleOrder(saleOrder);
			// Lua san pahm trong gio hang vao tbl_sale_order_product
			saleOrder.addRelationalSaleOrderProduct(product);
		}

		// Luu don hang
		ss.insert(saleOrder);

		// Xoa gio hang sau khi dat hang
		cart = new Cart();
		session.setAttribute("success", true);
		res.put("message", "Ban da dat hang thanh cong");
		return res;
	}

}
