package foodapp.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.WriteAbortedException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import foodapp.model.vo.Admin;
import foodapp.model.vo.Food;
import foodapp.model.vo.FoodMenu;
import foodapp.model.vo.User;

public class UserRepository {

	private final File dataFile=new File("Users.dat"); //List<User> 저장용 파일
 
	private List<User> users = new ArrayList<User>(); //User 리스트

	private FoodMenu foodMenu; //선택가능한 음식메뉴

	private String phone; //로그인 유저 폰번호 (User객체에 1:1맵핑)

	private final static int SEATS = 10; //좌석 수

	private boolean[] reservations = new boolean[SEATS]; //좌석 예약정보
	
	public UserRepository() {

		this.readFromFile();
		if(users.size() < 1) {
			User admin = new Admin("admin", "1234", "admin", "0", "0", false, 
					null, null, "0", 0, false, null, new TreeMap<Food, Integer>(), null);
			signUp(admin, admin.getPassword());
		}
		this.loadDefaultFoodMenu();
	}

	public void readFromFile() {
		if(dataFile.exists()==false)
			return;

		try (FileInputStream file=new FileInputStream(dataFile);
			ObjectInputStream in=new ObjectInputStream(file);){
			
			this.users = (ArrayList<User>)in.readObject();
			this.foodMenu = (FoodMenu)in.readObject();

		} catch(WriteAbortedException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public void storeToFile() {

		try (FileOutputStream file=new FileOutputStream(dataFile);
			ObjectOutputStream out=new ObjectOutputStream(file);){
			
			out.writeObject(this.users);
			out.writeObject(this.foodMenu);
			
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public boolean signUp(User user, String originalPassword) {
		if(this.phone != null) {
			System.out.println("회원가입 하려면 먼저 로그아웃 해주세요.");
			return false;
		}
		if(user == null) {
			System.out.println("사용자 데이터가 없습니다.");
		}
		else {
			try {
				String strongPassword = this.generateStorngPasswordHash(originalPassword);
				user.setPassword(strongPassword);
			} catch(InvalidKeySpecException e) {
				e.printStackTrace();
			} catch(NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		if(isDuplicate(user)) {
			System.out.println("이미 존재하는 회원 입니다.");
			return false;
		}
		else {
			users.add(user);
			System.out.println("회원가입이 성공적으로 처리되었습니다.");

			Collections.sort(this.users, (i,j)->{
				return i.getUsername().compareTo(j.getUsername());
			});
			return true;
		}

	}
	
	public User signIn(String phoneTextField, String passwordField) {
		if(phoneTextField.equals(this.phone)) {
			System.out.println("이미 로그인 되어 있습니다.");
			return null;
		}

		User user = getUserByPhoneAndPassword(phoneTextField, passwordField);

		if(user==null) {
			System.out.println("로그인에 실패 하였습니다.");
			return null;
		}
		else {
			user.setLogged(true);
			this.setPhone(phoneTextField);
			System.out.println("로그인 됐슨니다.");
			return user;
		}
	}

	public void logOff(String phoneNum) {
		if (!phoneNum.equals(this.phone)) {
			System.out.println("폰번호가 일치하지 않습니다.");
			System.out.println("로그아웃에 실패하였습니다.");
			return;
		}

		User user = null;
		Iterator<User>itr = users.iterator();
		while(itr.hasNext()) {	
			user = itr.next();
			if(user.getPhone().equals(this.phone)) {
				user.setLogged(false);
				user.setOrdering(false);
				this.setPhone(null);

				if(user.getSeatNo() >=1 && user.getSeatNo() <=reservations.length) {
					reservations[user.getSeatNo() -1] = false;
					user.setSeatNo(0);
				}
				System.out.println("로그아웃 됐습니다.");
				break;
			}
		}
	}

	public void showUsers() {
		System.out.println("이름\t전화\t이메일\t주소\t로그인상태\t주문날짜\t주문수단\t주문여부");

		Iterator<User> itr = users.iterator();
		User user = null;
		while(itr.hasNext()) {
			user = itr.next();
			user.showUserInfo();
		}
	}

	private void loadDefaultFoodMenu() {
		if(this.foodMenu != null && foodMenu.getFoodMenuList().size() > 0)
			return;
		foodMenu = new FoodMenu();

		foodMenu.addFood(new Food("NOODLE", 1, "짜장면", 5000));
		foodMenu.addFood(new Food("NOODLE", 2, "짬뽕", 6500));
		foodMenu.addFood(new Food("NOODLE", 3, "콩국수", 6000));
		foodMenu.addFood(new Food("NOODLE", 4, "비빔냉면", 6000));
		foodMenu.addFood(new Food("NOODLE", 5, "손칼국수", 5500));

		foodMenu.addFood(new Food("SOUP", 1, "김치찌개", 5500));
		foodMenu.addFood(new Food("SOUP", 2, "된장국", 5500));
		foodMenu.addFood(new Food("SOUP", 3, "황태국", 6500));

		foodMenu.addFood(new Food("RICE", 1, "햄볶음밥", 5000));
		foodMenu.addFood(new Food("RICE", 2, "제육덮밥", 6000));
		foodMenu.addFood(new Food("RICE", 3, "잡채밥", 6500));
		foodMenu.addFood(new Food("RICE", 4, "비빔밥", 5500));
		foodMenu.addFood(new Food("RICE", 5, "회덮밥", 8000));

		List<Food> menu = foodMenu.getFoodMenuList();
		foodMenu.setFoodMenuList(menu);
	}

	private User getUserByPhoneAndPassword(String phoneNum, String originalPassword) {
		User user = null;
		Iterator<User> itr = users.iterator();
		boolean valid= false;

		while(itr.hasNext()) {
			user = itr.next();
			if(user.getPhone().equals(phoneNum)) {
				try {
					valid = this.validatePassword(originalPassword, user.getPassword());
					
					if(valid) return user;
					else return null;

				} catch(InvalidKeySpecException e) {
					e.printStackTrace();
				} catch(NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}

	public User getUserByPhone(String phoneNum) {
		if (phoneNum == null) return null;

		User user = null;
		Iterator<User> itr = users.iterator();

		while(itr.hasNext()) {
			user = itr.next();
			if(user.getPhone().equals(phoneNum)) {
				return user;
			}
		}
		return null;
	}

	private boolean isDuplicate(User user) {
		Set<User> set = new HashSet<User>();
		for (User u: users) set.add(u);
		return !set.add(user);
	}

	/* password validation
	 * source : 
	 * https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
	 * */
	private String generateStorngPasswordHash(String password) 
			throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 500;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();
         
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }
     
    private byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
     
    private String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }

    private boolean validatePassword(String originalPassword, String storedPassword) 
    		throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);
         
        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();
         
        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public User getAdmin() {
		Iterator<User> itr = users.iterator();
		User user = null;
		while(itr.hasNext()) {
			user = itr.next();
			if(user.getUsername().equals("admin") && user.getPhone().equals("admin"))
				return user;
		}
		return null;
    }

	//getter setter
	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }
	public boolean[] getReservations() { return reservations; }
	public void setReservations(boolean[] reservations) { this.reservations = reservations; }
	public static int getSeats() { return SEATS; }
	public FoodMenu getFoodMenu() { return foodMenu; }
	public List<User> getUsers() { return users; }
}