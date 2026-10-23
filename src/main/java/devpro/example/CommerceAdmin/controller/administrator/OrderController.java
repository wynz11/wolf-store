package devpro.example.CommerceAdmin.controller.administrator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
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
import devpro.example.CommerceAdmin.model.OrderStatus;
import devpro.example.CommerceAdmin.model.SaleOrder;
import devpro.example.CommerceAdmin.model.User;
import devpro.example.CommerceAdmin.service.OrderService;
import devpro.example.CommerceAdmin.service.OrderStatusSerVice;
import devpro.example.CommerceAdmin.service.SaleOrderService;
import devpro.example.CommerceAdmin.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/order")

public class OrderController {

	@Autowired
	private OrderStatusSerVice osv;

	@Autowired
	private OrderService os;

	@Autowired
	private SaleOrderService sos;
	

	@Autowired
	private UserService us;

	@GetMapping({ "", "/" })
	public String home(Model model, HttpServletRequest request,

			// Sap xep va phan trang
			@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "8") int size,
			@RequestParam(defaultValue = "createDate") String column,
			@RequestParam(defaultValue = "desc") String direction,

			// Tim kiem va loc
			@RequestParam(required = false) String keyword, @RequestParam(required = false) Integer orderStatusId,
			@RequestParam(required = false) String status,

			// Loc theo ngay dat hang
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,

			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate) {

		// Chuyen status sang Boolean
		Boolean statusBool = null;

		if ("true".equalsIgnoreCase(status)) {
			statusBool = Boolean.TRUE;
		} else if ("false".equalsIgnoreCase(status)) {
			statusBool = Boolean.FALSE;
		}

		// Chuyen ngay sang LocalDateTime
		LocalDateTime fromInclusive = null;
		LocalDateTime toInclusive = null;

		if (fromDate != null) {
			fromInclusive = fromDate.atStartOfDay();
		}

		if (toDate != null) {
			toInclusive = toDate.atTime(LocalTime.MAX);
		}

		// Kiem tra page va size
		if (page < 0) {
			page = 0;
		}

		if (size <= 0) {
			size = 8;
		}

		// Chi cho phep sap xep theo cac truong hop le
		if (!"id".equals(column) && !"createDate".equals(column) && !"total".equals(column) && !"code".equals(column)) {
			column = "createDate";
		}

		// Sap xep
		Sort.Direction dir = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;

		// Phan trang
		Pageable pageable = PageRequest.of(page, size, Sort.by(dir, column));

		// Tim kiem, loc va phan trang
		Page<SaleOrder> ordersPage = sos.search(
		        keyword,
		        orderStatusId,
		        statusBool,
		        fromInclusive,
		        toInclusive,
		        pageable
		);

		// Du lieu bang don hang
		model.addAttribute("ordersPage", ordersPage);
		model.addAttribute("orders", ordersPage.getContent());

		// Thong tin phan trang va sap xep
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", size);
		model.addAttribute("column", column);
		model.addAttribute("direction", direction);
		model.addAttribute("nextDir", "asc".equalsIgnoreCase(direction) ? "desc" : "asc");

		// Gia tri tim kiem va bo loc
		model.addAttribute("keyword", keyword);
		model.addAttribute("orderStatusId", orderStatusId);
		model.addAttribute("status", status);
		model.addAttribute("fromDate", fromDate);
		model.addAttribute("toDate", toDate);
		
		// Thong ke don hang
		// Thong ke don hang
		model.addAttribute("totalOrders",
		        sos.getTotalOrders());

		model.addAttribute("processingOrders",
		        sos.getProcessingOrders());

		model.addAttribute("completedOrders",
		        sos.getCompletedOrders());

		model.addAttribute("cancelledOrders",
		        sos.getCancelledOrders());

		// Danh sach trang thai don hang
		model.addAttribute("orderStatuses", osv.findAll());
		
		model.addAttribute("orderStatuses", osv.findAll());
		model.addAttribute("orderStatusId", orderStatusId);

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
