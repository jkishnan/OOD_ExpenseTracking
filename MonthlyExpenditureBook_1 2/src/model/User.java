package model;

import java.util.ArrayList;
import java.util.List;

public class User {
	private static User instance;
    private static String username;
    private static String password;
    private List<Expenditure> expenditures;
    
    private static double budget;

	public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.expenditures = new ArrayList<>();
    }
	
	public static User getInstance() {
        if (instance == null) {
            try {
				instance = new User(username, password);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public List<Expenditure> getExpenditures() {
		return expenditures;
	}

	public void setExpenditures(List<Expenditure> expenditures) {
		this.expenditures = expenditures;
	}
}
