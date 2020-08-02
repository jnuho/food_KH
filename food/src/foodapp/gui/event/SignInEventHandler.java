package foodapp.gui.event;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import foodapp.dao.UserRepository;
import foodapp.model.vo.User;

public class SignInEventHandler extends MouseAdapter {
	private CardLayout cl;
	private JPanel userInputCards;
	private JButton signInBtn, logOffBtn;
	private JButton adminPageBtn;
	private JTextField phoneTextField;
	private JPasswordField passwordField;

	private UserRepository userRepo;

	public SignInEventHandler(CardLayout cl, JPanel userInputCards, JButton signInBtn, JButton logOffBtn, JButton adminPageBtn,
			JTextField phoneTextField, JPasswordField passwordField, UserRepository userRepo) {
		super();
		this.cl = cl;
		this.userInputCards = userInputCards;
		this.signInBtn = signInBtn;
		this.logOffBtn = logOffBtn;
		this.adminPageBtn = adminPageBtn;
		this.phoneTextField = phoneTextField;
		this.passwordField = passwordField;
		this.userRepo = userRepo;
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		userRepo.showUsers();

		if (phoneTextField == null) {
			return;
		}
		if(!phoneTextField.isEditable() || !passwordField.isEditable()) {
			System.out.println("이미 로그인 되어 있습니다.");
			JOptionPane.showMessageDialog(null, "이미 로그인 되어 있습니다.", "로그인 확인", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String phoneNum = phoneTextField.getText().replaceAll("\\s+", "");
		String password = new String(passwordField.getPassword());
		User user = null;
		if (phoneNum.length() > 0 && password.length() >0) {
			user = userRepo.signIn(phoneNum, password);
		}
		else {
			JOptionPane.showMessageDialog(null, "아이디 비밀번호를 모두 입력해주세요", "로그인 확인", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(user!=null) {
			phoneTextField.setEditable(false);
			passwordField.setEditable(false);
			if(user.equals(userRepo.getAdmin()))
				this.adminPageBtn.setVisible(true);
			else
				this.adminPageBtn.setVisible(false);
			signInBtn.setVisible(false);
			logOffBtn.setVisible(true);

			cl.show(userInputCards, "USER_LOGGED");

			for(Component c : userInputCards.getComponents()) {
				if(c instanceof JPanel) {
					for(Component btn : ((JPanel) c).getComponents()) {
						if(btn instanceof JButton) {
							ImageIcon icon = new ImageIcon(this.getClass().getResource("../images/online.png"));
							icon = new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
							((JButton)btn).setIcon(icon);
							((JButton) btn).setText("Hello! " + user.getUsername());
							((JButton)btn).setHorizontalAlignment(SwingConstants.RIGHT);
						}
					}
				}
				
			}
			JOptionPane.showMessageDialog(null, "로그인을 성공했습니다.", "로그인 확인", JOptionPane.WARNING_MESSAGE);
		}
		else {
			JOptionPane.showMessageDialog(null, "로그인 정보가 불일치 합니다.", "로그인 확인", JOptionPane.WARNING_MESSAGE);
			phoneTextField.setText("");
			passwordField.setText("");
		}
	}
	
}