package foodapp.gui.layout;

import static foodapp.dao.Constants.ADD_FOOD;
import static foodapp.dao.Constants.ADMIN_PAGE;
import static foodapp.dao.Constants.CARD;
import static foodapp.dao.Constants.CASH;
import static foodapp.dao.Constants.FOOD_MENU_PAGE;
import static foodapp.dao.Constants.INIT_PAGE;
import static foodapp.dao.Constants.NOODLE;
import static foodapp.dao.Constants.NOODLE_TABLE;
import static foodapp.dao.Constants.ORDER;
import static foodapp.dao.Constants.CANCLE_ORDER;
import static foodapp.dao.Constants.ORDER_VIEW_PAGE;
import static foodapp.dao.Constants.RICE;
import static foodapp.dao.Constants.RICE_TABLE;
import static foodapp.dao.Constants.SIGN_UP_PAGE;
import static foodapp.dao.Constants.SOUP;
import static foodapp.dao.Constants.SOUP_TABLE;
import static foodapp.dao.Constants.WINDOW_HEIGHT;
import static foodapp.dao.Constants.WINDOW_WIDTH;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import foodapp.dao.UserRepository;
import foodapp.gui.event.SignInEventHandler;
import foodapp.gui.event.SignOffEventHandler;
import foodapp.model.vo.Admin;
import foodapp.model.vo.Food;
import foodapp.model.vo.FoodMenu;
import foodapp.model.vo.User;


public class InitPageFrame extends JFrame implements MouseListener {

	private static final long serialVersionUID = 1L;

	private JSplitPane mainSplitPane1, mainSplitPane2, mainSplitPane3;
	private JSplitPane subSplitPane1, subSplitPane2, subSplitPane5;

	private JPanel pageViewCards;
	private JPanel pageCard1, pageCard2, pageCard3, pageCard4;

	private JPanel menuTableCards;
	private JPanel noodleCard, soupCard, riceCard;
	
	private JPanel userInputCards;
	private JPanel userLogOffCard, userLoggedCard;
	
	private JPanel topPanel, bottomPanel, rightPanel, centerAndRightPanel, leftPanel;
	private JPanel centerPanel;
	private JPanel p1, p2, p3, p4, p5, p6, p7;
	
	private JButton foodMenuBtn, adminPageBtn, orderViewBtn;
	private JButton signInBtn, signUpBtn, logOffBtn, welcomeBtn;
	private JButton orderBtn, cancelOrderBtn;
	
	private JLabel subTotalLabel;
	private JPanel subTotalPanel;
	private JTextArea popularMenuTextArea;
	private JPanel popularMenuPanel;

	private JPanel orderSelectionPanel;
	
	private JLabel menuCategoryLabel, menuChoiceLabel, menuQtyLabel;
	private JTextField menuCategoryTxt;
	private JTextField subMenuTxt;
	private JToggleButton payCardBtn, payCashBtn;
	private ButtonGroup payMethodBtnGrp;
	
	private JButton addMenuBtn;
	
	private JPanel menuCategoryPanel, menuChoicePanel, menuQtyPanel, payMethodPanel;
	
	private JButton noodleBtn, soupBtn, riceBtn;
	
	private DefaultTableModel modelN, modelS, modelR, modelOrdering;
	
	private JComboBox<Integer> menuQtyComboBox;

	private JTextField phoneTextField;
	private JPasswordField passwordField;
	private JLabel phoneLabel, passwordLabel;
	
	private JTable menuNoodleTable, menuSoupTable, menuRiceTable, orderingTable;
	private JScrollPane scrollNoodlePane, scrollSoupPane, scrollRicePane, scrollOrderingPane;
	
	private JLabel fancyLabel;
	
	private UserRepository userRepo = null;
	
	private Map<Food, Integer> tempOrderList;

	/* CONSTRUCTOR */
	public InitPageFrame(UserRepository userRepo) throws Exception {
		this.userRepo = userRepo;
		initialize();
		setPopularMenuList();
	}

	private void initialize() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocationRelativeTo(null); //center window
		getContentPane().setLayout(new BorderLayout());

		/* 첫번째 JSplitPane */
		createFirstTop();
		createFirstBottom();
		createFirstSP();

		/* 두번째 JSplitPane */
		createSecondTop();
		createSecondBottom();
		createSecondSP();

		/* 세번째 JSplitPane */
		createThirdTop();
		createThirdBottom();
		createThirdSP();

		/* 생성한 JSplitPane의 border 설정 */
		invokeSplitPane();

		/* CardLayout 만들기 */
		updateCards();

		/* window closing */
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				User user = null;
				if (userRepo.getPhone() != null) {
					user = (User)userRepo.getUserByPhone(userRepo.getPhone());
					if (user == null)
						return;
					if(user.isOrdering())
						user.setOrdering(false);

					user.setLogged(false);
				}
				userRepo.storeToFile();
			}
		});

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void createFirstSP() {
		mainSplitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		mainSplitPane1.setTopComponent(topPanel);
		mainSplitPane1.setBottomComponent(bottomPanel);
	}

	private void createFirstTop() {
		createFirstTopLeft();
		createFirstTopCenter();
		createFirstTopRight();

		subSplitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		centerAndRightPanel = new JPanel(new GridLayout(1,2));
		subSplitPane2.setLeftComponent(leftPanel);
		centerAndRightPanel.add(centerPanel);
		centerAndRightPanel.add(rightPanel);
		subSplitPane2.setRightComponent(centerAndRightPanel);

		topPanel = new JPanel(new BorderLayout());
		topPanel.add(subSplitPane2);
	}
	
	private void createFirstTopLeft() {
		leftPanel = new JPanel(new GridLayout(3,1));
		ImageIcon icon = null;
		icon = new ImageIcon(getClass().getResource("../images/noodle.jpg"));
		icon = new ImageIcon(icon.getImage().getScaledInstance(180, 106, Image.SCALE_SMOOTH));
		noodleBtn = new JButton(icon); noodleBtn.setName(NOODLE);
		leftPanel.add(noodleBtn);

		icon = new ImageIcon(getClass().getResource("../images/soup.jpg"));
		icon = new ImageIcon(icon.getImage().getScaledInstance(180, 106, Image.SCALE_SMOOTH));
		soupBtn = new JButton(icon); soupBtn.setName(SOUP);
		leftPanel.add(soupBtn);

		icon = new ImageIcon(getClass().getResource("../images/rice.png"));
		icon = new ImageIcon(icon.getImage().getScaledInstance(180, 106, Image.SCALE_SMOOTH));
		riceBtn = new JButton(icon); riceBtn.setName(RICE);
		leftPanel.add(riceBtn);

		noodleBtn.addMouseListener(this);
		soupBtn.addMouseListener(this);
		riceBtn.addMouseListener(this);
	}

	private void createFirstTopCenter() {
		CardLayout layout = new CardLayout();
		menuTableCards = new JPanel(layout);
		menuTableCards.setBorder(new EmptyBorder(0,0,0,0));

		noodleCard = new JPanel(new BorderLayout());
		noodleCard.setName(NOODLE);
		soupCard = new JPanel(new BorderLayout());
		soupCard.setName(SOUP);
		riceCard = new JPanel(new BorderLayout());
		riceCard.setName(RICE);
		
		//create table models
		Map<String, DefaultTableModel> tableModels = this.constructTableModels();
		modelN = tableModels.get(NOODLE);
		modelS = tableModels.get(SOUP);
		modelR = tableModels.get(RICE);

		//create tables
		menuNoodleTable = new JTable(modelN);
		menuSoupTable = new JTable(modelS);
		menuRiceTable = new JTable(modelR);

		menuNoodleTable.setName(NOODLE_TABLE);
		menuSoupTable.setName(SOUP_TABLE);
		menuRiceTable.setName(RICE_TABLE);

		menuNoodleTable.setAutoCreateRowSorter(true);
		menuSoupTable.setAutoCreateRowSorter(true);
		menuRiceTable.setAutoCreateRowSorter(true);

		menuNoodleTable.addMouseListener(this);
		menuSoupTable.addMouseListener(this);
		menuRiceTable.addMouseListener(this);

		//put in scroll panes
		scrollNoodlePane = new JScrollPane(menuNoodleTable);
		scrollSoupPane = new JScrollPane(menuSoupTable);
		scrollRicePane = new JScrollPane(menuRiceTable);

		scrollNoodlePane.setBorder(new EmptyBorder(0,0,0,0));
		scrollSoupPane.setBorder(new EmptyBorder(0,0,0,0));
		scrollRicePane.setBorder(new EmptyBorder(0,0,0,0));

		//put in card layout panel
		noodleCard.add(scrollNoodlePane);
		soupCard.add(scrollSoupPane);
		riceCard.add(scrollRicePane);

		menuTableCards.add(noodleCard, NOODLE);
		menuTableCards.add(soupCard,SOUP);
		menuTableCards.add(riceCard, RICE);
		
		//add menu button
		ImageIcon icon = new ImageIcon(getClass().getResource("../images/add.png"));
		icon = new ImageIcon(icon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		addMenuBtn = new JButton(icon);
		addMenuBtn.setName(ADD_FOOD);
		addMenuBtn.setFocusPainted(false);
		addMenuBtn.addMouseListener(this);
		addMenuBtn.setBorder(new EmptyBorder(0,7,0,7));
		p5 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p5.setBorder(new EmptyBorder(0,0,0,0));
		p5.add(addMenuBtn);

		p3 = new JPanel(new BorderLayout());
		p3.add(menuTableCards, BorderLayout.CENTER);
		p3.add(p5, BorderLayout.SOUTH);
		
		orderSelectionPanel = new JPanel(new GridLayout(4,1));

		menuCategoryLabel = new JLabel("메뉴 카테고리");
		menuCategoryTxt = new JTextField("", 10);
		menuCategoryTxt.setEditable(false);

		for(Component card : menuTableCards.getComponents()) {
			if(card instanceof JPanel && card.isVisible()) {
				switch(card.getName()){
					case NOODLE: this.menuCategoryTxt.setText("면 메뉴"); break;
					case SOUP: this.menuCategoryTxt.setText("탕 메뉴"); break;
					case RICE: this.menuCategoryTxt.setText("밥 메뉴"); break;
				}
			}
		}

		menuChoiceLabel = new JLabel("선택 메뉴");
		subMenuTxt = new JTextField(10);
		subMenuTxt.setEditable(false);

		menuQtyLabel = new JLabel("수 량");
		ComboBoxModel<Integer> comboBoxModel = 
				new DefaultComboBoxModel<Integer>(new Integer[] {1,2,3,4,5});
		menuQtyComboBox = new JComboBox<Integer>(comboBoxModel);
		
		payCardBtn = new JToggleButton("카드 주문");
		payCardBtn.setName(CARD);
		payCardBtn.setBackground(Color.WHITE);
		payCashBtn = new JToggleButton("현금 주문");
		payCashBtn.setName(CASH);
		payCashBtn.setBackground(Color.WHITE);

		payMethodBtnGrp = new ButtonGroup();
		payMethodBtnGrp.add(payCardBtn);
		payMethodBtnGrp.add(payCashBtn);
		

		menuCategoryPanel = new JPanel(new GridLayout(1,2));
		menuChoicePanel = new JPanel(new GridLayout(1,2));
		menuQtyPanel = new JPanel(new GridLayout(1,2));
		payMethodPanel = new JPanel(new GridLayout(1,2));
		
		JPanel l1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel l2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel l3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		l1.add(menuCategoryLabel);
		l2.add(menuChoiceLabel);
		l3.add(menuQtyLabel);

		menuCategoryPanel.add(l1); menuCategoryPanel.add(menuCategoryTxt);
		menuChoicePanel.add(l2); menuChoicePanel.add(subMenuTxt);
		menuQtyPanel.add(l3); menuQtyPanel.add(menuQtyComboBox);
		payMethodPanel.add(payCardBtn); payMethodPanel.add(payCashBtn);
		
		orderSelectionPanel.add(menuCategoryPanel);
		orderSelectionPanel.add(menuChoicePanel);
		orderSelectionPanel.add(menuQtyPanel);
		orderSelectionPanel.add(payMethodPanel);
		
		subSplitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		subSplitPane1.setTopComponent(p3);
		subSplitPane1.setBottomComponent(orderSelectionPanel);

		centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(subSplitPane1, BorderLayout.CENTER);
	}

	private void createFirstTopRight() {
		ImageIcon icon = new ImageIcon(getClass().getResource("../images/delete.png"));
		icon = new ImageIcon(icon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		cancelOrderBtn = new JButton(icon);
		cancelOrderBtn.setName(CANCLE_ORDER);
		cancelOrderBtn.setBorder(new EmptyBorder(0,0,0,0));
		cancelOrderBtn.setFocusPainted(false);
		cancelOrderBtn.addMouseListener(this);

		p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p2.add(cancelOrderBtn);

		subTotalLabel = new JLabel("");

		subTotalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		subTotalPanel.add(subTotalLabel);

		p6 = new JPanel(new GridLayout(1,2));
		p6.setBorder(new EmptyBorder(0,0,0,0));
		p6.add(p2);
		p6.add(subTotalPanel);

		modelOrdering = constructOrderingTableModel();
		orderingTable = new JTable(modelOrdering);
		orderingTable.setName("ORDERING_TABLE");
		orderingTable.setAutoCreateRowSorter(true);
		scrollOrderingPane = new JScrollPane(orderingTable);
		scrollOrderingPane.setBorder(new EmptyBorder(0,0,0,0));
		
		p7 = new JPanel(new BorderLayout());
		p7.add(scrollOrderingPane, BorderLayout.CENTER);
		p7.add(p6, BorderLayout.SOUTH);

		icon = new ImageIcon(getClass().getResource("../images/screen.jpg"));
		icon = new ImageIcon(icon.getImage().getScaledInstance(340, 170, Image.SCALE_SMOOTH));
		fancyLabel = new JLabel(icon);

		rightPanel = new JPanel(new GridLayout(2,1));
		rightPanel.add(fancyLabel);
		rightPanel.add(p7);
	}

	private void createFirstBottom() {
		popularMenuTextArea = new JTextArea(200, 300);
		popularMenuTextArea.setEditable(false);
		popularMenuPanel = new JPanel(new BorderLayout());
		popularMenuPanel.add(popularMenuTextArea);

		orderBtn = new JButton("주문 하기");
		orderBtn.setName(ORDER);
		
		subSplitPane5 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		subSplitPane5.setTopComponent(popularMenuPanel);
		subSplitPane5.setBottomComponent(orderBtn);

		bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(subSplitPane5);

		orderBtn.addMouseListener(this);
	}
	
	private void createSecondSP() {
		mainSplitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		mainSplitPane2.setTopComponent(topPanel);
		mainSplitPane2.setBottomComponent(bottomPanel);
	}
	
	private void createSecondTop() {
		/* 네비게이션 메뉴 */
		JToolBar navBar = this.createNavBar();
		navBar.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

		topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		topPanel.add(navBar);

		foodMenuBtn.addMouseListener(this);
		orderViewBtn.addMouseListener(this);
		adminPageBtn.addMouseListener(this);
	}

	private void createSecondBottom() {
		bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(mainSplitPane1);
	}
	
	private void createThirdSP() {
		mainSplitPane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		mainSplitPane3.setTopComponent(topPanel);
		mainSplitPane3.setBottomComponent(bottomPanel);
	}

	private void createThirdTop() {
		CardLayout cl = new CardLayout();
		userInputCards = new JPanel(cl);

		phoneTextField = new JTextField("", 11); //핸드폰 11자리
		passwordField = new JPasswordField(11);

		Font font = new Font("바탕체", Font.BOLD, 11);
		phoneLabel = new JLabel("핸드폰 번호");
		phoneLabel.setFont(font);
		passwordLabel = new JLabel("비밀번호");
		passwordLabel.setFont(font);

		userLogOffCard = new JPanel(new FlowLayout(FlowLayout.LEFT));
		userLogOffCard.add(phoneLabel);
		userLogOffCard.add(phoneTextField);
		userLogOffCard.add(passwordLabel);
		userLogOffCard.add(passwordField);

		welcomeBtn = new JButton("Hello!");
		welcomeBtn.setBorder(new EmptyBorder(0,0,0,0));
		userLoggedCard = new JPanel(new FlowLayout(FlowLayout.LEFT));
		userLoggedCard.add(welcomeBtn);

		userInputCards.add(userLogOffCard, "USER_LOGOFF");
		userInputCards.add(userLoggedCard, "USER_LOGGED");

		signInBtn = new JButton("로그인");
		signInBtn.setFont(font);
		logOffBtn = new JButton("로그아웃");
		logOffBtn.setFont(font);
		logOffBtn.setVisible(false);
		signUpBtn = new JButton("회원가입");
		signUpBtn.setFont(font);
		signUpBtn.setName(SIGN_UP_PAGE);

		p1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		p1.add(signInBtn);
		p1.add(logOffBtn);
		p1.add(signUpBtn);

		p4 = new JPanel(new GridLayout(1,2));
		p4.add(userInputCards);
		p4.add(p1);

		topPanel = new JPanel(new BorderLayout());
		topPanel.add(p4);

		signInBtn.addMouseListener(
				new SignInEventHandler(
						(CardLayout)userInputCards.getLayout(), 
						userInputCards,
						signInBtn,
						logOffBtn,
						adminPageBtn, 
						phoneTextField, 
						passwordField, 
						userRepo));
		
		logOffBtn.addMouseListener(
				new SignOffEventHandler(
						(CardLayout)userInputCards.getLayout(), 
						userInputCards,
						signInBtn,
						logOffBtn,
						adminPageBtn, 
						phoneTextField, 
						passwordField, 
						modelOrdering, 
						userRepo));

		signUpBtn.addMouseListener(this);
	}

	private void createThirdBottom() {
		bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(mainSplitPane2);
	}
	
	private JToolBar createNavBar() {
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		
		ImageIcon icon = null;

		//food menu btn
		icon = new ImageIcon(getClass().getResource("../images/menu.png"));
		icon = new ImageIcon(icon.getImage().getScaledInstance(43, 43, Image.SCALE_SMOOTH));
		foodMenuBtn = new JButton(icon);
		foodMenuBtn.setName(FOOD_MENU_PAGE);
		
		JPanel panel = new JPanel();
		panel.add(foodMenuBtn);

		toolbar.add(panel);
		toolbar.add(new JSeparator());

		//order view btn
		icon = new ImageIcon(getClass().getResource("../images/orderView.png"));
		icon = new ImageIcon(icon.getImage().getScaledInstance(43, 43, Image.SCALE_SMOOTH));
		orderViewBtn = new JButton(icon);
		orderViewBtn.setName(ORDER_VIEW_PAGE);

		panel = new JPanel();
        panel.add(orderViewBtn); //add button to panel

		toolbar.add(panel);
		toolbar.add(new JSeparator());
		
        //admin page btn
		icon = new ImageIcon(getClass().getResource("../images/admin.png"));
		icon = new ImageIcon(icon.getImage().getScaledInstance(43, 43, Image.SCALE_SMOOTH));
		adminPageBtn = new JButton(icon);
		adminPageBtn.setName(ADMIN_PAGE);
		adminPageBtn.setVisible(false);

		panel = new JPanel();
        panel.add(adminPageBtn); //add button to panel

		toolbar.add(panel);

		return toolbar;
	}
	
	private TableModel createDefaultTableModel() {
		String[] colNames = {"카테고리", "메뉴번호", "메뉴이름", "가격"};

        return new DefaultTableModel(colNames, 0) {
			private static final long serialVersionUID = 1L;
			String[] colNames = {"카테고리", "메뉴번호", "메뉴이름", "가격"};
			@Override
			public String getColumnName(int column) {
				return colNames[column];
			}
		    @Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
        };
	}

	private DefaultTableModel constructOrderingTableModel() {
		String[] colNames = {"카테고리", "메뉴번호", "메뉴이름", "가격", "개수"};
        modelOrdering = new DefaultTableModel(colNames, 0) {
			private static final long serialVersionUID = 1L;
			String[] colNames = {"카테고리", "메뉴번호", "메뉴이름", "가격", "개수"};
			@Override
			public String getColumnName(int column) {
				return colNames[column];
			}
		    @Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
        };

		return modelOrdering;
	}

	private Map<String, DefaultTableModel> constructTableModels() {
		FoodMenu menu = userRepo.getFoodMenu();
		List<Food> foodMenuList = null;
		Iterator<Food> itr = null;
		//Food 리스트 데이터를 가져옵니다.
		if(menu!= null)
			foodMenuList = menu.getFoodMenuList();

		//Food의 compareTo정의된 정렬 방법에 따라 Food 리스트 데이터를 정렬합니다.
		if(foodMenuList != null) {
			Collections.sort(foodMenuList, (i,j)->{
				return i.compareTo(j);
			});

			itr = foodMenuList.iterator();
		}

		//데이터를 넣을 테이블 모델을 정의합니다.
		Food food = null;
        modelN = (DefaultTableModel)createDefaultTableModel();
        modelS = (DefaultTableModel)createDefaultTableModel();
        modelR = (DefaultTableModel)createDefaultTableModel();

		//3가지 메뉴별로 row 데이터 저장
		while(itr!= null && itr.hasNext()) {
			food = itr.next();

			String[] temp = new String[] { food.getMenuCategory(), 
					String.valueOf(food.getMenuNo()), 
					food.getMenuName(), 
					food.toCurrency(food.getMenuPrice()) };
			switch(food.getMenuCategory()) {
				case NOODLE: modelN.addRow(temp); break;
				case SOUP: modelS.addRow(temp); break;
				case RICE: modelR.addRow(temp); break;
			}
		}
		
		//만들어진 테이블 모델 데이터를 TreeMap에 담아서 return 합니다.
		Map<String, DefaultTableModel> map = new TreeMap<String, DefaultTableModel>();
		map.put(NOODLE, modelN);
		map.put(SOUP, modelS);
		map.put(RICE, modelR);

		return map;
	}

	private void invokeSplitPane() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
				mainSplitPane1.setDividerLocation(330 + mainSplitPane1.getInsets().top);
				mainSplitPane1.setDividerSize(1);
				mainSplitPane1.setEnabled(false);

				mainSplitPane2.setDividerLocation(67 + mainSplitPane2.getInsets().top);
				mainSplitPane2.setDividerSize(1);
				mainSplitPane2.setEnabled(false);

				mainSplitPane3.setDividerLocation(35 + mainSplitPane3.getInsets().top);
				mainSplitPane3.setDividerSize(1);
				mainSplitPane3.setEnabled(false);

				subSplitPane1.setDividerLocation(200 + subSplitPane1.getInsets().top);
				subSplitPane1.setDividerSize(1);
				subSplitPane1.setEnabled(false);

				subSplitPane2.setDividerLocation(.2);
				subSplitPane2.setDividerSize(1);
				subSplitPane2.setEnabled(false);

				subSplitPane5.setDividerLocation(120+ subSplitPane5.getInsets().top);
				subSplitPane5.setDividerSize(1);
				subSplitPane5.setEnabled(false);
            }
        });
   }

	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		Object source = e.getSource();
		if (source instanceof JButton) {
			String name = ((JButton) e.getSource()).getName();
			switch(name) {
				case FOOD_MENU_PAGE: createFoodMenuPage(); break;
				case ORDER_VIEW_PAGE: createOrderViewPage(); break;
				case ADMIN_PAGE: createAdminPage(); break;
				case SIGN_UP_PAGE: createSignUpPage(); break;
				case NOODLE: showNoodleMenu(); break;
				case SOUP: showSoupMenu(); break;
				case RICE: showRiceMenu(); break;
				case ADD_FOOD: addFoodOrderList(); break;
				case ORDER: completeOrder(); break;
				case CANCLE_ORDER: cancelOrder(); break;
				default:
					break;
			}
		}
		else if(source instanceof JTable) {
			DefaultTableModel model = null;
			JTable table = (JTable)e.getSource();
			String name = table.getName();
			switch(name) {
				case NOODLE_TABLE: model = (DefaultTableModel)menuNoodleTable.getModel(); break;
				case SOUP_TABLE: model = (DefaultTableModel)menuSoupTable.getModel(); break;
				case RICE_TABLE: model = (DefaultTableModel)menuRiceTable.getModel(); break;
				default:
					break;
			}

			int row = table.getSelectedRow();

			Object o1 = model.getValueAt(row, 1);
			Object o2 = model.getValueAt(row, 2);
			if(o1 == null || o2 == null) return;
			this.subMenuTxt.setText(o1.toString() + ". " + o2.toString());
		}
	}
	
	private void cancelOrder() {
		cancelOrderBtn.setFocusPainted(false);
		int row = orderingTable.getSelectedRow();

		if (row == -1) {
			JOptionPane.showMessageDialog(null, "주문취소할 음식을 먼저 클릭해주세요.", "음식 미선택", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String menuCategory = (String)modelOrdering.getValueAt(row, 0);
		int menuNo = Integer.valueOf((String)modelOrdering.getValueAt(row, 1));
		String menuName=  (String)modelOrdering.getValueAt(row, 2);
		int menuPrice = 0;
		String temp = ((String)modelOrdering.getValueAt(row, 3)).substring(1);
		menuPrice = Integer.valueOf(temp.replace(",", ""));

		Food food = new Food(menuCategory, menuNo, menuName, menuPrice);
		tempOrderList.remove(food);

		int subTotal = 0;
		for(Map.Entry<Food, Integer> entry : tempOrderList.entrySet()) {
			subTotal += entry.getKey().getMenuPrice() * entry.getValue();
		}

		this.subTotalLabel.setText("    누적 금액  " + 
			NumberFormat.getCurrencyInstance(Locale.KOREA).format(subTotal).toString());
		
		modelOrdering.removeRow(row);
		modelOrdering.fireTableDataChanged();
		
		if(modelOrdering.getRowCount() == 0) {
			User user = userRepo.getUserByPhone(this.phoneTextField.getText());
			user.setOrdering(false);
		}
	}

	private void addFoodOrderList() {
		addMenuBtn.setFocusPainted(false);
		if(this.phoneTextField.isEditable() && this.passwordField.isEditable()) {
			JOptionPane.showMessageDialog(null, "로그인이 필요합니다.", "로그인 확인", JOptionPane.WARNING_MESSAGE);
			return;
		}

		User user = userRepo.getUserByPhone(this.phoneTextField.getText());

		if (user == null) {
			JOptionPane.showMessageDialog(null, "핸드폰정보 유저 없음", "로그인 확인", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if(!user.isOrdering()) {
			this.tempOrderList = new TreeMap<Food, Integer>();
			user.setOrdering(true);
		}

		TableModel model = null;
		int row = 0;
		
		for(Component card : menuTableCards.getComponents()) {
			if(card instanceof JPanel && card.isVisible()) {
				switch(card.getName()){
					case NOODLE:model = (DefaultTableModel)menuNoodleTable.getModel();
								row = menuNoodleTable.getSelectedRow();
								break;
					case SOUP:  model = (DefaultTableModel)menuSoupTable.getModel();
							   	row = menuSoupTable.getSelectedRow();
							   	break;
					case RICE:  model = (DefaultTableModel)menuRiceTable.getModel();
								row = menuRiceTable.getSelectedRow();
								break;
				}
			}
		}

		if (row==-1) {
			JOptionPane.showMessageDialog(null, "주문할 음식을 먼저 클릭해주세요.", "음식 미선택", JOptionPane.WARNING_MESSAGE);
			return;
		}
		String menuCategory = (String)model.getValueAt(row, 0);
		int menuNo = Integer.valueOf((String)model.getValueAt(row, 1));
		String menuName=  (String)model.getValueAt(row, 2);
		int menuPrice = 0;
		String temp = ((String)model.getValueAt(row, 3)).substring(1);
		menuPrice = Integer.valueOf(temp.replace(",", ""));

		Food food = new Food(menuCategory, menuNo, menuName, menuPrice);
		
		tempOrderList.put(food, (int)this.menuQtyComboBox.getSelectedItem());
		
		updateOrderingTable(food, (int)this.menuQtyComboBox.getSelectedItem());

		int subTotal = 0;
		for(Map.Entry<Food, Integer> entry : tempOrderList.entrySet()) {
			subTotal += entry.getKey().getMenuPrice() * entry.getValue();
		}

		Font font = new Font("맑은고딕", Font.BOLD, 12);
        subTotalLabel.setFont(font);

		this.subTotalLabel.setText("     누적 금액  " + 
			NumberFormat.getCurrencyInstance(Locale.KOREA).format(subTotal).toString());

		JOptionPane.showMessageDialog(null, "주문이 추가되었습니다.", "주문 추가 확인", JOptionPane.WARNING_MESSAGE);
		return;
	}

	private void completeOrder() {
		if(this.phoneTextField.getText() == "") {
			JOptionPane.showMessageDialog(null, "로그인이 필요합니다.", "로그인 확인", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if(!payCardBtn.isSelected() && !payCashBtn.isSelected()) {
			JOptionPane.showMessageDialog(null, "결제수단을 선택해 주세요.", "결제 수단 선택", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String payMethod = this.payCardBtn.isSelected()? CARD : CASH;

		User user = userRepo.getUserByPhone(this.phoneTextField.getText());
		if(user == null)
			return;
		if(!user.isOrdering()) {
			JOptionPane.showMessageDialog(null, "음식 메뉴를 추가해 주세요.", "메뉴 미지정", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int result = JOptionPane.showConfirmDialog(null, "주문을 완료하시겠습니까?", "주문 확인",
				JOptionPane.OK_CANCEL_OPTION);

		if(result!= JOptionPane.OK_OPTION) {
			return;
		}

		Map<Food, Integer> orderList = tempOrderList;

		User admin = userRepo.getAdmin();

		Map<Food, Integer> salesResult = ((Admin)admin).getSalesResult();
		if (salesResult == null)
			((Admin) admin).setSalesResult(new TreeMap<Food, Integer>());

		Food food = null;
		int qty  =0;
		for(Map.Entry<Food, Integer> entry : orderList.entrySet()) {
			food = entry.getKey();
			qty = entry.getValue();

			if(salesResult.get(food) != null)
				salesResult.put(food, salesResult.get(food) + qty);
			else
				salesResult.put(food, qty);
		}
		((Admin)admin).setSalesResult(salesResult);
		user.setOrderList(tempOrderList);
		user.setRecentPayMethod(payMethod);
		user.setOrdering(false);
		user.setOrderCreated(new GregorianCalendar());

		Map<GregorianCalendar, Map<Food, Integer>> orderHistory = user.getOrderHistory();
		if (orderHistory == null) {
			orderHistory = new TreeMap<GregorianCalendar, Map<Food, Integer>>();
		}
		
		userRepo.showUsers();
		setPopularMenuList();
		
		modelOrdering.setRowCount(0);

		this.subTotalLabel.setText("");

		JOptionPane.showMessageDialog(null, "주문이 완료 되었습니다.", "주문결제 완료 확인", JOptionPane.WARNING_MESSAGE);
		return;
	}

	private void updateOrderingTable(Food food, int qty) {
		boolean duplicate = false;
		String menuCategory = "", menuName = "";
		int menuNo =0, menuPrice = 0;
		
		String[] orderingItem = new String[] {
				food.getMenuCategory(), 
				String.valueOf(food.getMenuNo()), 
				food.getMenuName(), 
				NumberFormat.getCurrencyInstance(Locale.KOREA).format(food.getMenuPrice()),
				String.valueOf(qty),};
		for(int i =0; i<modelOrdering.getRowCount(); i++) {
			menuCategory = (String)modelOrdering.getValueAt(i, 0);
			menuNo = Integer.valueOf((String)modelOrdering.getValueAt(i, 1));
			menuName = (String)modelOrdering.getValueAt(i, 2);
			menuPrice = Integer.valueOf(((String)modelOrdering.getValueAt(i, 3)).substring(1).replace(",", ""));
			
			if(orderingItem[0].equals(menuCategory)
					&& Integer.valueOf(orderingItem[1]) == menuNo
					&& orderingItem[2].equals(menuName)
					&& food.getMenuPrice() == menuPrice) {
				duplicate = true;
				modelOrdering.setValueAt(qty, i, 4);
			}
		}

		if(!duplicate)
			modelOrdering.addRow(orderingItem);

		modelOrdering.fireTableDataChanged();
	}

	private void showNoodleMenu() {
		menuNoodleTable.getSelectionModel().clearSelection();
		this.subMenuTxt.setText("");
		CardLayout cl = (CardLayout)(menuTableCards.getLayout());
        cl.show(menuTableCards, NOODLE);
        this.menuCategoryTxt.setText("면 메뉴");
	}

	private void showSoupMenu() {
		menuSoupTable.getSelectionModel().clearSelection();
		this.subMenuTxt.setText("");
		CardLayout cl = (CardLayout)(menuTableCards.getLayout());
        cl.show(menuTableCards, SOUP);
        this.menuCategoryTxt.setText("탕 메뉴");
	}

	private void showRiceMenu() {
		menuRiceTable.getSelectionModel().clearSelection();
		this.subMenuTxt.setText("");
		CardLayout cl = (CardLayout)(menuTableCards.getLayout());
        cl.show(menuTableCards, RICE);
        this.menuCategoryTxt.setText("밥 메뉴");
	}

	private void createAdminPage() {
		adminPageBtn.setFocusPainted(false);
		if(phoneTextField.getText() == null
				|| !phoneTextField.getText().equals("admin")) {
			JOptionPane.showMessageDialog(null, "관리자로 먼저 로그인 해주세요.", "관리자 권한 에러", JOptionPane.WARNING_MESSAGE);
			return;
		}

		JFrame frame = new AdminPageFrame(modelN, modelS, modelR, popularMenuTextArea, userRepo);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) { 
			}
		});

		frame.setSize(500, 500);
		frame.setLocation(this.getX() + 50 , this.getY() + 90);
		frame.setLocationRelativeTo(null);

		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true); 
	}

	private void updateCards() {
		CardLayout cl = null;
		if(pageViewCards == null
				|| !(pageViewCards.getLayout() instanceof CardLayout)) 
			cl = new CardLayout();
		else
			cl = (CardLayout)pageViewCards.getLayout();


		pageViewCards = new JPanel(cl);

		pageCard1 = new JPanel(new BorderLayout());
		pageCard2 = new JPanel(new BorderLayout());
		pageCard3 = new JPanel(new BorderLayout());
		pageCard4 = new JPanel(new BorderLayout());

		pageCard1.add(mainSplitPane3);
		pageCard2.add(new FoodMenuViewCard(cl, pageViewCards, userRepo));
		pageCard3.add(new OrderViewCard(cl, pageViewCards, phoneTextField, userRepo));
		pageCard4.add(new SignUpViewCard(cl, pageViewCards, userRepo));

		pageViewCards.add(pageCard1, INIT_PAGE);
		pageViewCards.add(pageCard2, FOOD_MENU_PAGE);
		pageViewCards.add(pageCard3, ORDER_VIEW_PAGE);
		pageViewCards.add(pageCard4, SIGN_UP_PAGE);

		add(pageViewCards);

		pageViewCards.revalidate();
	}

	private void createFoodMenuPage() {
		CardLayout cl = (CardLayout)(pageViewCards.getLayout());
		updateCards();
        cl.show(pageViewCards, FOOD_MENU_PAGE);
	}

	private void createOrderViewPage() {
		CardLayout cl = (CardLayout)(pageViewCards.getLayout());
		updateCards();
        cl.show(pageViewCards, ORDER_VIEW_PAGE);
	}
	
	private void createSignUpPage() {
		CardLayout cl = (CardLayout)(pageViewCards.getLayout());
        cl.show(pageViewCards, SIGN_UP_PAGE);
	}

	private void setPopularMenuList() {
		Map<Food, Integer> salesResult = ((Admin)userRepo.getAdmin()).getSalesResult();
		if (salesResult == null)
			return;

		List<Map.Entry<Integer, Food>> sortedSales = new ArrayList<Map.Entry<Integer, Food>>();

		Map.Entry<Integer, Food> newEntry = null;
		for(Map.Entry<Food, Integer> entry : salesResult.entrySet()) {
			newEntry = new AbstractMap.SimpleEntry<Integer, Food>(entry.getValue(), entry.getKey());
			sortedSales.add(newEntry);
		}
		
		Collections.sort(sortedSales, (i,j)->{
			return j.getKey() - i.getKey();
		});

		int count = 0;
		int qty = 0;
		String msg = "    ★  인기 메뉴\n";
		Food food = null;
		Iterator<Map.Entry<Integer, Food>> itr = sortedSales.iterator();
		this.popularMenuTextArea.setText("");

		while(itr.hasNext() && count++ < 5) {
			newEntry = itr.next();
			food = newEntry.getValue();
			qty = newEntry.getKey();
			msg += "    " + count+ "등 메뉴:   " + food.toString() + " - - - 총 " + qty + " 개 팔렸어요.";
			if(count <5) msg+="\n";
		}
		popularMenuTextArea.setText(msg);
		Font font = new Font("맑은고딕", Font.BOLD, 11);
        popularMenuTextArea.setFont(font);
        popularMenuTextArea.setForeground(Color.BLUE);
	}

}