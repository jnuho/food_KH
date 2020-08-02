package foodapp.main;

import foodapp.dao.UserRepository;
import foodapp.gui.layout.InitPageFrame;

public class Main {
	private static UserRepository userRepo = new UserRepository();

	public UserRepository getUserRepository() {
		return userRepo;
	}

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	try {
					new InitPageFrame(userRepo);
            	} catch(Exception e) {
            		e.printStackTrace();
            	}
            }
        });
	}
}
