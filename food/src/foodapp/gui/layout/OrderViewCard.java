package foodapp.gui.layout;

import static foodapp.dao.Constants.INIT_PAGE;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import foodapp.dao.UserRepository;
import foodapp.model.vo.Food;
import foodapp.model.vo.User;

public class OrderViewCard extends JPanel implements MouseListener{

	private static final long serialVersionUID = 1L;

	private CardLayout cl;
	private JPanel pageViewCards;
	private JTextField phoneTextField;
	

	private JTextArea receiptTextArea;
	private JPanel orderViewLeftPanel;
	private JSplitPane centerReceipt;

	private JPanel receiptLabelPanel;
	private JLabel receiptLabel;

	private JSplitPane orderViewSplitPane1, orderViewSplitPane2, orderViewSplitPane3;
	private JPanel orderViewRightPanel;
		
	private JPanel backButtonPanel;
	private JButton orderViewHomeBtn;

	private UserRepository userRepo;

	public OrderViewCard(CardLayout cl, JPanel pageViewCards, 
			JTextField phoneTextField, UserRepository userRepo) {
		this.cl = cl;
		this.pageViewCards = pageViewCards;
		this.phoneTextField = phoneTextField;
		this.userRepo = userRepo;
		
		initialize();
	}
	
	private void initialize() {
		createOrderViewPage();
		showOrderViewPage();
	}

	private void createOrderViewPage() {
		orderViewLeftPanel = new JPanel();
		centerReceipt = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		Font font = new Font("맑은고딕", Font.BOLD, 17);
		receiptLabel = new JLabel("최근 주문 내역");
		receiptLabel.setFont(font);
		receiptLabel.setForeground(Color.DARK_GRAY);
		receiptLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		receiptLabelPanel.add(receiptLabel);
		receiptTextArea = new JTextArea(400, 500);
		receiptTextArea.setEditable(false);
		font = new Font("맑은고딕", Font.BOLD, 13);
        receiptTextArea.setFont(font);
        receiptTextArea.setForeground(Color.BLUE);

		centerReceipt.setTopComponent(receiptLabelPanel);
		centerReceipt.setBottomComponent(receiptTextArea);

		orderViewSplitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		orderViewSplitPane1.setLeftComponent(orderViewLeftPanel);
		orderViewSplitPane1.setRightComponent(centerReceipt);

		orderViewSplitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		orderViewRightPanel = new JPanel();
		orderViewSplitPane2.setLeftComponent(orderViewSplitPane1);
		orderViewSplitPane2.setRightComponent(orderViewRightPanel);
		
		orderViewSplitPane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		orderViewHomeBtn = new JButton("뒤로 가기");
		orderViewHomeBtn.setName(INIT_PAGE);
		backButtonPanel.add(orderViewHomeBtn);
		orderViewSplitPane3.setTopComponent(backButtonPanel);
		orderViewSplitPane3.setBottomComponent(orderViewSplitPane2);
		
		invokeSplitPane();
		
		orderViewHomeBtn.addMouseListener(this);

		setLayout(new BorderLayout());
		add(orderViewSplitPane3);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}

		setVisible(true);
	}

	private void invokeSplitPane() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                orderViewSplitPane1.setDividerLocation(150 + orderViewSplitPane1.getInsets().left);
                orderViewSplitPane2.setDividerLocation(675+orderViewSplitPane2.getInsets().left);
                orderViewSplitPane3.setDividerLocation(40+orderViewSplitPane3.getInsets().top);
                centerReceipt.setDividerLocation(30+centerReceipt.getInsets().top);

                orderViewSplitPane1.setEnabled(false);
                orderViewSplitPane1.setDividerSize(1);

                orderViewSplitPane2.setEnabled(false);
                orderViewSplitPane2.setDividerSize(1);

                orderViewSplitPane3.setEnabled(false);
                orderViewSplitPane3.setDividerSize(1);
                
                centerReceipt.setEnabled(false);
                centerReceipt.setDividerSize(1);
            }
        });
    }

	private void showOrderViewPage() {
		if(this.phoneTextField.getText().equals("")) {
			receiptTextArea.setText("");
			return;
		}
		User user = userRepo.getUserByPhone(this.phoneTextField.getText());
		if(user ==null) {
			JOptionPane.showMessageDialog(null, "로그인이 필요합니다.", "로그인 확인", JOptionPane.WARNING_MESSAGE);
			return;
		}
		Map<Food, Integer> orderList = user.getOrderList();

		if(orderList == null || orderList.size() < 1) {
			receiptTextArea.setText("");
			return;
		}
		
		Date temp = new Date(user.getOrderCreated().getTimeInMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yy년 MM월 dd일, HH시 mm분 ss초");
		String date = sdf.format(temp);
		String msg = "\t주문 날짜:  " + date + "\n\n";
		Food food = null;
		int qty = 0;
		int sum = 0;
		for(Map.Entry<Food, Integer> entry : orderList.entrySet()) {
			food = entry.getKey();
			qty = entry.getValue();
			msg += "\t" + food + " * " + qty + " 개.\n";
			sum += food.getMenuPrice() * qty;
		}
		msg += "\n\t 총 주문 액: " + NumberFormat.getCurrencyInstance(Locale.KOREA).format(sum);

		receiptTextArea.setText(msg);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		cl.show(pageViewCards, INIT_PAGE);
	}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
}
