package foodapp.gui.event;

import java.awt.CardLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import foodapp.dao.UserRepository;

public class SignOffEventHandler extends MouseAdapter {
	private CardLayout cl;
	private JPanel userInputCards;
	private JButton signInBtn, logOffBtn;
	private JButton adminPageBtn;
	private DefaultTableModel modelOrdering;
	private JTextField loggedPhoneTextField;
	private JPasswordField passwordField;
	private UserRepository userRepo;

	public SignOffEventHandler(CardLayout cl, JPanel userInputCards, JButton signInBtn, JButton logOffBtn,
			JButton adminPageBtn, JTextField loggedPhoneTextField, JPasswordField passwordField, 
			DefaultTableModel modelOrdering, UserRepository userRepo) {
		super();

		this.cl = cl;
		this.userInputCards = userInputCards;
		this.signInBtn = signInBtn;
		this.logOffBtn = logOffBtn;
		this.adminPageBtn = adminPageBtn;
		this.modelOrdering = modelOrdering;
		this.loggedPhoneTextField = loggedPhoneTextField;
		this.passwordField = passwordField;
		this.userRepo = userRepo;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (loggedPhoneTextField != null) {
			String phoneNum = loggedPhoneTextField.getText().replaceAll("\\s+", "");
			if(!passwordField.isEditable() && phoneNum.length() > 0) {
				userRepo.logOff(phoneNum);
				loggedPhoneTextField.setEditable(true);
				loggedPhoneTextField.setText("");
				signInBtn.setVisible(true);
				logOffBtn.setVisible(false);
				passwordField.setEditable(true);
				passwordField.setText("");
				modelOrdering.setRowCount(0);
				this.adminPageBtn.setVisible(false);
				cl.show(userInputCards, "USER_LOGOFF");
			}
		}
	}
	
}