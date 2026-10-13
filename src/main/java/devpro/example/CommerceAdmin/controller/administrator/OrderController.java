package devpro.example.CommerceAdmin.controller.administrator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import devpro.example.CommerceAdmin.dto.OrderDto;
import devpro.example.CommerceAdmin.model.Category;
import devpro.example.CommerceAdmin.model.Order;
import devpro.example.CommerceAdmin.model.User;
import devpro.example.CommerceAdmin.service.OrderService;
import devpro.example.CommerceAdmin.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/order")

public class OrderController {

	@Autowired
	private OrderService os;

	@Autowired
	private UserService us;

	@GetMapping({ "/", "" })
	public String home(final Model model, HttpServletRequest request,

			// Phân trang
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size,

			// Sắp xếp
			@RequestParam(defaultValue = "id") String column, @RequestParam(defaultValue = "desc") String direction,

			// Tìm kiếm
			@RequestParam(required = false) String keyword,
			// Trạng thái đơn hàng
			@RequestParam(required = false) String status) {

		// Trạng thái
		String statusFilter = null;

		if (status != null && !status.isEmpty()) {
			statusFilter = status;
		}

		// Sắp xếp
		Sort.Direction dir = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

		// Phân trang
		Pageable pageable = PageRequest.of(page, size, Sort.by(dir, column));

		// Search + Paging
		Page<Order> ordersPage = os.search(
				keyword, 
				statusFilter, 
				pageable);

		List<Order> orders = os.findAll();

		model.addAttribute("ordersPage", ordersPage);
		model.addAttribute("orders", ordersPage.getContent());

		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", size);

		model.addAttribute("column", column);
		model.addAttribute("direction", direction);

		model.addAttribute("keyword", keyword);
		model.addAttribute("status", status);

		model.addAttribute("totalOrders", os.getTotalOrders());

		model.addAttribute("processingOrders", os.getProcessingOrders());

		model.addAttribute("completedOrders", os.getCompletedOrders());

		model.addAttribute("cancelledOrders", os.getCancelledOrders());

		return "administrator/order/order";
	}

	@GetMapping("/add")
	public String addNewOrder(final Model model) {

		OrderDto orderDto = new OrderDto();

		List<User> users = us.findAll();

		model.addAttribute("orderDto", orderDto);
		model.addAttribute("users", users);

		return "administrator/order/add";
	}

	@PostMapping("/save")
	public String saveNewOrder(Model model, @Valid @ModelAttribute OrderDto orderDto, BindingResult result) {

		if (result.hasErrors()) {
			model.addAttribute("users", us.findAll());
			return "administrator/order/add";
		}

		try {
			os.insert(orderDto);
		} catch (Exception e) {
			e.printStackTrace();

			model.addAttribute("users", us.findAll());
			return "administrator/order/add";
		}

		return "redirect:/admin/order";

	}

	@GetMapping("/edit")
	public String editOrder(final Model model, @RequestParam Integer id) {

		Order order = os.findById(id);

		if (order == null) {
			return "redirect:/admin/order";
		}

		OrderDto orderDto = new OrderDto();

		orderDto.setOrderDate(order.getOrderDate());
		orderDto.setTotalAmount(order.getTotalAmount());
		orderDto.setStatus(order.getStatus());
		orderDto.setUserId(order.getUser() != null ? order.getUser().getId() : null);
		orderDto.setPaymentMethod(order.getPaymentMethod());
		orderDto.setShippingAddress(order.getShippingAddress());
		orderDto.setNote(order.getNote());

		model.addAttribute("orderDto", orderDto);
		model.addAttribute("order", order);
		List<User> users = us.findAll();
		model.addAttribute("users", users);

		return "administrator/order/edit";

	}

	@PostMapping("/update")
	public String saveEditOrder(Model model, @Valid @ModelAttribute OrderDto orderDto, BindingResult result,
			@RequestParam Integer id) {

		if (id == null || id < 1) {
			return "redirect:/admin/order";
		}

		Order order = new Order();
		order.setId(id);

		if (result.hasErrors()) {
			model.addAttribute("orderDto", orderDto);
			model.addAttribute("order", order);
			List<User> users = us.findAll();
			model.addAttribute("users", users);

			return "administrator/order/edit";
		}

		try {
			os.update(orderDto, id);
		} catch (Exception e) {
			e.printStackTrace();

			model.addAttribute("orderDto", orderDto);
			model.addAttribute("order", order);
			List<User> users = us.findAll();
			model.addAttribute("users", users);

			return "administrator/order/edit";
		}

		return "redirect:/admin/order";
	}

	@GetMapping("/view")
	public String viewOrder(final Model model, @RequestParam Integer id) {

		Order order = os.findById(id);

		if (order == null) {
			return "redirect:/admin/order";
		}

		model.addAttribute("order", order);

		return "administrator/order/view";

	}

	@GetMapping("/delete")
	public String deleteOrder(@RequestParam Integer id) {

		if (id != null && id > 0) {
			os.inactive(id);
		}

		return "redirect:/admin/order";
	}

}
