package foodapp.model.vo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

public class Admin extends User {

	private static final long serialVersionUID = 1L;

	//메뉴, 판매량
	private Map<Food, Integer> salesResult;
	private Map<GregorianCalendar, Map<Food,Integer>> salesHistory;

	public Admin(String username, String password, String phone, String email, String address, boolean logged,
			Map<Food, Integer> orderList, GregorianCalendar orderCreated, String recentPayMethod, int seatNo,
			boolean ordering, Map<GregorianCalendar, Map<Food,Integer>> orderHistory, 
			Map<Food, Integer> salesResult, Map<GregorianCalendar, Map<Food,Integer>> salesHistory) {
		super(username, password, phone, email, address, logged, orderList, orderCreated, recentPayMethod, seatNo,
				ordering, orderHistory);

		this.salesResult = salesResult;
		this.setSalesHistory(salesHistory);
	}
	
	@Override
	public void showUserInfo() {
		super.showUserInfo();
//		this.showSalesResult(this.salesResult);
		this.showSalesHistory();
	}

	public void showSalesResult(Map<Food, Integer> salesResult) {
		if(salesResult != null && salesResult.size() > 0) {
			System.out.println("\t--- 매출액 ---");
			for(Map.Entry<Food, Integer> entry : salesResult.entrySet())
				System.out.println("\t" + entry.getKey() + "  :  총 " + entry.getValue() + " 개 판매.");
		}
	}
	
	public void showSalesHistory() {
		if(salesHistory != null && salesHistory.size() > 0) {
			String date = "";
			System.out.println("\n\t --- 매출 history ---");
			for(Map.Entry<GregorianCalendar, Map<Food, Integer>> entry : salesHistory.entrySet()) {
				if(entry.getKey() !=null) {
					Date temp = new Date(entry.getKey().getTimeInMillis());
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss yy-MM-dd");
					date = sdf.format(temp);
					System.out.println("\t" + date);
					showSalesResult(entry.getValue());
				}
			}
			
		}
	}
	
	public Map<Food, Integer> getSalesResult() { return (TreeMap<Food, Integer>)salesResult; } 
	public void setSalesResult(Map<Food, Integer> salesResult) { this.salesResult = salesResult; }
	public Map<GregorianCalendar, Map<Food,Integer>> getSalesHistory() { return salesHistory; } 
	public void setSalesHistory(Map<GregorianCalendar, Map<Food,Integer>> salesHistory) { this.salesHistory = salesHistory; }
}