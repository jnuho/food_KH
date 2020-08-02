package foodapp.model.vo;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

@SuppressWarnings("serial")
public class Food implements Serializable, Comparable<Food> {

	private String menuCategory;
	private int menuNo;
	private String menuName;
	private int menuPrice;


	public Food() {}	
	
	public Food(String menuCategory, int menuNo, String menuName, int menuPrice) {
		super();
		this.menuCategory = menuCategory;
		this.menuNo = menuNo;
		this.menuName = menuName;
		this.menuPrice = menuPrice;
	}

	@Override
	public boolean equals(Object obj) {
		Food food = (Food)obj;
		return this.menuCategory.equals(food.getMenuCategory())
				&& this.menuNo == food.getMenuNo()
				&& this.menuName.equals(food.getMenuName());
	}
	
	@Override
	public int hashCode() {
		return menuCategory.length() + menuNo + menuName.length();
	}
	
	@Override
	public String toString() {
		return "[" + menuCategory + "]  " + menuNo + ". " + menuName + " --- " + toCurrency(menuPrice);
	}
	
	@Override
	public int compareTo(Food o) {
		return this.menuCategory.compareTo(o.getMenuCategory()) == 0 ? 
					this.menuNo - o.getMenuNo(): this.menuCategory.compareTo(o.getMenuCategory());
	}

	public String toCurrency(int price) {
		return NumberFormat.getCurrencyInstance(Locale.KOREA).format(price);
	}

	public String getMenuCategory() { return menuCategory; }
	public void setMenuCategory(String menuCategory) { this.menuCategory = menuCategory; }
	public int getMenuNo() { return menuNo; }
	public void setMenuNo(int menuNo) { this.menuNo = menuNo; }
	public String getMenuName() { return menuName; } 
	public void setMenuName(String menuName) { this.menuName = menuName; } 
	public int getMenuPrice() { return menuPrice; } 
	public void setMenuPrice(int menuPrice) { this.menuPrice = menuPrice; }
}