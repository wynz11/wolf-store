package devpro.example.CommerceAdmin.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import devpro.example.CommerceAdmin.dto.Cart;
import devpro.example.CommerceAdmin.model.User;
import jakarta.servlet.http.HttpSession;

@Controller
public class BaseController {

	public Cart getCart(HttpSession session) {
		Cart cart = (Cart) session.getAttribute("CART");
		if (cart == null) {// Chua tao gio hang thi tao moi
			cart = new Cart();
			session.setAttribute("CART", cart);// Tao bien session
		}
		return cart;
	}

	@ModelAttribute("totalProducts") // dung ModelAttribute de nhan totalProducts
	public Integer totalProducts(HttpSession session) {

		Cart cart = getCart(session);
		if (cart == null)
			return 0;
		return cart.getTotalProducts();
	}

	// Lay thong tin user dang nhap
	@ModelAttribute("loginedUser")
	public User getLoginedUser() {
		Object loginedUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (loginedUser != null && loginedUser instanceof UserDetails) {
			User user = (User) loginedUser;
			return user;
		}
		return new User();
	}

	// Kiem tra da login hay chua?
	@ModelAttribute("isLogined")
	public boolean isLogined() {
		Object loginedUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (loginedUser != null && loginedUser instanceof UserDetails) {
			return true;
		}
		return false;
	}

}
