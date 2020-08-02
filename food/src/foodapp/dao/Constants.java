package foodapp.dao;

public final class Constants {
	private Constants() {
		//restrict initialization
	}

	/* 메인화면 크기 */
	public static final int 		WINDOW_WIDTH 		= 830;
	public static final int 		WINDOW_HEIGHT 		= 650;

	/* 화면 이름 */
	public static final String 		INIT_PAGE 			= "Card - Home Page";
	public static final String 		FOOD_MENU_PAGE 		= "Card - Food Menu";
	public static final String 		ORDER_VIEW_PAGE 	= "Card - View Order";
	public static final String 		SIGN_UP_PAGE 		= "Card - Sign Up";
	public static final String 		ADMIN_PAGE 			= "Card - Admin Page";

	/* User 로그인 상태 */
	public static final boolean		OFF					= false;
	public static final boolean		ON					= true;
	
	/* User 지불 수단 */
	public static final String		CARD				= "CARD";
	public static final String		CASH				= "CASH";

	/* Food 메뉴 카테고리 */
	public static final String		NOODLE				= "NOODLE";
	public static final String		SOUP				= "SOUP";
	public static final String		RICE				= "RICE";
	
	/* 주문 시 버튼이름 */
	public static final String		ADD_FOOD			= "ADD_FOOD";
	public static final String		ORDER				= "ORDER";
	public static final String		CANCLE_ORDER		= "CANCLE_ORDER";
	
	/* 면,탕,밥 메뉴 테이블 */
	public static final String		NOODLE_TABLE		= "NOODLE_TABLE";
	public static final String		SOUP_TABLE			= "SOUP_TABLE";
	public static final String		RICE_TABLE			= "RICE_TABLE";
	
}