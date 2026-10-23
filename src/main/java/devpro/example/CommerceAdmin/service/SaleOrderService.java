package devpro.example.CommerceAdmin.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import devpro.example.CommerceAdmin.model.OrderStatus;
import devpro.example.CommerceAdmin.model.SaleOrder;
import devpro.example.CommerceAdmin.repository.SaleOrderRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

@Service
public class SaleOrderService {

	private final SaleOrderRepository srp;

	public SaleOrderService(SaleOrderRepository srp) {
		this.srp = srp;
	}

	public List<SaleOrder> findAll() {
		return srp.findAll();
	}

	public SaleOrder findById(Integer id) {
		return srp.findById(id).orElse(null);
	}

	public SaleOrder insert(SaleOrder saleOrder) {
		return srp.save(saleOrder);
	}

	public Page<SaleOrder> findAll(int page, int size, String column, String direction) {
		Sort sort = "desc".equalsIgnoreCase(direction) ? Sort.by(column).descending() : Sort.by(column).ascending();
		Pageable pageable = PageRequest.of(page, size, sort);
		return srp.findAll(pageable);
	}

	// Tim kiem, sap xep va phan trang
	// Tim kiem, loc va phan trang
	public Page<SaleOrder> search(String keyword, Integer orderStatusId, Boolean status, LocalDateTime fromDate,
			LocalDateTime toDate, Pageable pageable) {

		Specification<SaleOrder> spec = (root, query, cb) -> cb.conjunction();

		// Tim kiem theo thong tin don hang

		// Tim kiem theo thong tin don hang
		if (keyword != null && !keyword.trim().isEmpty()) {

			String searchText = keyword.trim().toLowerCase();
			String kw = "%" + searchText + "%";

			spec = spec.and((root, q, cb) -> {
				List<Predicate> ors = new ArrayList<>();

				// Tim theo ma don hang ORD1, ORD2, ORD3...
				if (searchText.matches("ord\\d+")) {
					Integer orderId = Integer.parseInt(searchText.substring(3));
					ors.add(cb.equal(root.get("id"), orderId));
				}

				// Tim theo cac thong tin khac
				ors.add(cb.like(cb.lower(root.get("code")), kw));
				ors.add(cb.like(cb.lower(root.get("customerName")), kw));
				ors.add(cb.like(cb.lower(root.get("customerMobile")), kw));
				ors.add(cb.like(cb.lower(root.get("customerEmail")), kw));
				ors.add(cb.like(cb.lower(root.get("customerAddress")), kw));
				ors.add(cb.like(cb.lower(root.get("description")), kw));

				Join<SaleOrder, OrderStatus> os = root.join("orderStatus", JoinType.LEFT);

				ors.add(cb.like(cb.lower(os.get("name")), kw));

				return cb.or(ors.toArray(new Predicate[0]));
			});
		}

		// Loc theo trang thai don hang
		if (orderStatusId != null && orderStatusId != 0) {
			spec = spec.and((root, q, cb) -> {
				Join<SaleOrder, OrderStatus> os = root.join("orderStatus", JoinType.INNER);

				return cb.equal(os.get("id"), orderStatusId);
			});
		}

		// Loc theo trang thai hoat dong
		if (status != null) {
			spec = spec.and((root, q, cb) -> cb.equal(root.get("status"), status));
		}

		// Loc tu ngay
		if (fromDate != null) {
			spec = spec.and((root, q, cb) -> cb.greaterThanOrEqualTo(root.get("createDate"), fromDate));
		}

		// Loc den ngay
		if (toDate != null) {
			spec = spec.and((root, q, cb) -> cb.lessThanOrEqualTo(root.get("createDate"), toDate));
		}

		return srp.findAll(spec, pageable);
	}

	// Thong ke tong so don hang
	public long getTotalOrders() {
		return srp.count();
	}

	public long getProcessingOrders() {
		return srp.countByOrderStatus_Name("Đơn hàng mới/Chờ xử lý");
	}

	public long getCompletedOrders() {
		return srp.countByOrderStatus_Name("Đã giao");
	}

	public long getCancelledOrders() {
		return srp.countByOrderStatus_Name("Đã hủy");
	}

//	public Boolean existsByName(String name) {
//		return srp.existsByName(name);
//	}
//	
	public Boolean isExistFile(MultipartFile file) {
		if (file != null && file.getOriginalFilename() != null && file.getOriginalFilename().length() > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean isExistFiles(MultipartFile[] files) {
		if (files != null && files.length > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
