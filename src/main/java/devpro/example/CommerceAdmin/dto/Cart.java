package devpro.example.CommerceAdmin.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart {
	private List<CartItem> items = new ArrayList<CartItem>();

	public List<CartItem> getItems() {
		return items;
	}

	public void setItems(List<CartItem> items) {
		this.items = items;
	}

	public int findById(Integer productId) {
		for (int i = 0; i < this.items.size(); i++) {
			if (this.items.get(i).getId() == productId) {
				return i;
			}
		}
		return -1;
	}

	public BigDecimal getTotalCartPrice() {
		BigDecimal total = BigDecimal.ZERO;
		for (CartItem item : this.items) {
			total = total.add(item.getTotalPrice());
		}
		return total;
	}

	public BigDecimal getGrandTotal() {
		return getTotalCartPrice();
	}

	public int getTotalProducts() {
		Integer total = 0;
		for (CartItem item : this.items) {
			total += item.getQuantity();
		}
		return total;
	}

	public void removeItem(Integer productId) {
		items.removeIf(item -> item.getId() != null && item.getId().equals(productId));
	}

}
