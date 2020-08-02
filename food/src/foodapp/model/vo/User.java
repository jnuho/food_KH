package foodapp.model.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

@SuppressWarnings("serial")
public class User implements Serializable, Comparable<User> {
	private String username;
	private String phone;
	private String password;
	private String email;
	private String address;
	private boolean logged; //로그인 상태 여부
	private Map<Food, Integer> orderList;
	private GregorianCalendar orderCreated;
	private String recentPayMethod;
	private int seatNo; //좌석(1~SEATS)
	private boolean ordering;
	private Map<GregorianCalendar, Map<Food, Integer>> orderHistory;

	public User(String username, String password, String phone, String email, String address, boolean logged,
			Map<Food, Integer> orderList, GregorianCalendar orderCreated, String recentPayMethod, int seatNo, 
			boolean ordering, Map<GregorianCalendar, Map<Food, Integer>> orderHistory) {
		super();
		this.username = username;
		this.password = password;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.logged = logged;
		this.orderList = orderList;
		this.orderCreated = orderCreated;
		this.recentPayMethod = recentPayMethod;
		this.seatNo = seatNo;
		this.ordering = ordering;
		this.orderHistory = orderHistory;
	}

	public void showUserInfo() {
		String date = "";
		if(orderCreated !=null) {
			Date temp = new Date(orderCreated.getTimeInMillis());
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss yy-MM-dd");
			date = sdf.format(temp);
		}
		System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n",
							username, phone, email, address,
							(logged? "로그ON":"로그OFF"), date, recentPayMethod, ordering? "주문중 O" : "주문중 X");
		if(this.orderList != null)
			this.showOrderList(this.orderList);
		if(this.orderHistory != null)
			this.showOrderHistory();
	}
	
	public void showOrderList(Map<Food,Integer> orderList) {
		for(Map.Entry<Food, Integer> entry : orderList.entrySet())
			System.out.println("\t" + entry.getKey() + "  :  " +  entry.getValue() + "개 주문.");
	}
	
	public void showOrderHistory() {
		String date = "";
		for(Map.Entry<GregorianCalendar, Map<Food, Integer>> entry: orderHistory.entrySet()) {
			if(entry.getKey() != null) {
				Date temp = new Date(entry.getKey().getTimeInMillis());
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss yy-MM-dd");
				date = sdf.format(temp);
				System.out.println("\t" + date);
				showOrderList(entry.getValue());
			}
		}
		
	}

	@Override
	public int hashCode() {
		return this.phone.length();
	}

	@Override
	public boolean equals(Object obj) {
		User user = (User)obj;
		if(this.phone.equals(user.getPhone()))
			return true;
		else
			return false;
	}
	
	@Override
	public int compareTo(User o) {
		return this.getUsername().compareTo(o.getUsername());
	}

	//getter setter
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getAddress() { return address; }
	public void setAddress(String address) { this.address = address; }
	public boolean isLogged() { return logged; } 
	public void setLogged(boolean logged) { this.logged = logged; }
	public Map<Food, Integer> getOrderList() { return orderList; }
	public void setOrderList(Map<Food, Integer> orderList) { this.orderList = orderList; }
	public GregorianCalendar getOrderCreated() { return orderCreated; }
	public void setOrderCreated(GregorianCalendar orderCreated) { this.orderCreated = orderCreated; }
	public String getRecentPayMethod() { return recentPayMethod; } 
	public void setRecentPayMethod(String recentPayMethod) { this.recentPayMethod = recentPayMethod; } 
	public int getSeatNo() { return seatNo; }
	public void setSeatNo(int seatNo) { this.seatNo = seatNo; }
	public boolean isOrdering() { return ordering; }
	public void setOrdering(boolean ordering) { this.ordering = ordering; }
	public Map<GregorianCalendar, Map<Food, Integer>> getOrderHistory() { return orderHistory; } 
	public void setOrderHistory(Map<GregorianCalendar, Map<Food, Integer>> orderHistory) { this.orderHistory = orderHistory; }
}
