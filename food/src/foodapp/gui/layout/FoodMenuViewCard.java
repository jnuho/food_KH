package foodapp.gui.layout;

import static foodapp.dao.Constants.INIT_PAGE;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import foodapp.dao.UserRepository;
import foodapp.model.vo.Food;
import foodapp.model.vo.FoodMenu;

public class FoodMenuViewCard extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;

	private JButton logoBtn;
	
	private CardLayout cl;
	private JPanel pageViewCards;

	private JPanel orderViewLeftPanel;
	private JPanel orderViewRightPanel;
	private JSplitPane centerReceipt;

	private JPanel receiptLabelPanel;
	private JLabel receiptLabel;
	private JTextPane receiptTextArea;

	private JSplitPane orderViewSplitPane1, orderViewSplitPane2, orderViewSplitPane3;

	private JPanel backButtonPanel;

	private UserRepository userRepo;

	public FoodMenuViewCard(CardLayout cl, JPanel pageViewCards, UserRepository userRepo) {
		this.cl = cl;
		this.pageViewCards = pageViewCards;
		this.userRepo = userRepo;

		initialize();
	}

	private void initialize(){
		orderViewLeftPanel = new JPanel();
		centerReceipt = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

		receiptLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		receiptLabel = new JLabel("전체 메뉴");
		receiptLabelPanel.add(receiptLabel);
		receiptTextArea = new JTextPane();
		Font font = new Font("맑은고딕", Font.BOLD, 13);
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
		logoBtn = new JButton("뒤로 가기");
		logoBtn.setName(INIT_PAGE);
		logoBtn.addMouseListener(this);
		backButtonPanel.add(logoBtn);
		orderViewSplitPane3.setTopComponent(backButtonPanel);
		orderViewSplitPane3.setBottomComponent(orderViewSplitPane2);
		
		invokeSplitPane();

		showMenuList();
		
		setLayout(new BorderLayout());
		add(orderViewSplitPane3);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}

		setVisible(true);
	}

	private void showMenuList() {
		FoodMenu menu = userRepo.getFoodMenu();

		if(menu ==null || menu.getFoodMenuList() == null)
			return;

		List<Food> foodMenuList = (ArrayList<Food>)userRepo.getFoodMenu().getFoodMenuList();
		Collections.sort(foodMenuList, (i,j)->{
			return i.compareTo(j);
		});

		Iterator<Food> itr = foodMenuList.iterator();
		Food food = null;
		String msg = "";

		receiptTextArea.setText("");
		receiptTextArea.setEditable(true);

		int count = 0;
		Color[] colors = {Color.BLUE, Color.ORANGE, Color.RED,};
		String menuCategory = null;

		receiptTextArea.setText("");


		while(itr.hasNext()) {
			food = itr.next();
				
			msg = "\t"+food + "\n";
			if(menuCategory != null && !menuCategory.equals(food.getMenuCategory())) {
				count++;
				msg = "\n" + msg;
			}
			menuCategory = food.getMenuCategory();

			appendToPane(receiptTextArea, msg, colors[count]);
		}
		receiptTextArea.setEditable(false);

	}

	private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }

	private void invokeSplitPane() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                orderViewSplitPane1.setDividerLocation(200 + orderViewSplitPane1.getInsets().left);
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