package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class UserDatabase {

	private static UserDatabase instance;
	private Map<String, User> users;

	private UserDatabase() {
		users = new HashMap<>();
		// Add some sample users
		users.put("romil", new User("romil", "123456"));
		users.put("huizen", new User("huizen", "123456"));
		users.put("jaya", new User("jaya", "123456"));
	}

	public static UserDatabase getInstance() {
		if (instance == null) {
			instance = new UserDatabase();
		}
		return instance;
	}

	public boolean authenticate(String username, String password) {
		User user = users.get(username);
		if (user == null) {
			return false;
		}
		return user.getPassword().equals(password);
	}

	public boolean signUp(String username, String password) {
		if (users.containsKey(username)) {
			return false;
		}
		users.put(username, new User(username, password));
		return true;
	}

	public void exportExpenditures(User user, File file) throws IOException {
        List<Expenditure> expenditures = user.getExpenditures();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write header row
            writer.write("Date,Amount,Description,Category,Recurring\n");

            // Write data rows
            for (Expenditure expenditure : expenditures) {
                String date = dateFormatter.format(expenditure.getDate());
                String amount = String.valueOf(expenditure.getAmount());
                String description = expenditure.getDescription();
                String category = expenditure.getCategory();
                String recurring = String.valueOf(expenditure.isRecurring());

                writer.write(date + "," + amount + "," + description + "," + category + "," + recurring + "\n");
            }
        }
    }
	
	public void importExpenditures(User user, File file) throws IOException {
	    List<Expenditure> newExpenditures = new ArrayList<>();

	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

	    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	        // Read header row (skip it)
	        reader.readLine();

	        // Read data rows
	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] values = line.split(",");

	            if (values.length != 5) {
	                throw new IOException("Invalid CSV format");
	            }

	            LocalDate date;
	            try {
	                date = LocalDate.parse(values[0], dateFormatter);
	            } catch (DateTimeParseException e) {
	                throw new IOException("Invalid date format");
	            }

	            double amount;
	            try {
	                amount = Double.parseDouble(values[1]);
	            } catch (NumberFormatException e) {
	                throw new IOException("Invalid amount format");
	            }

	            String description = values[2];
	            String category = values[3];

	            boolean recurring;
	            try {
	                recurring = Boolean.parseBoolean(values[4]);
	            } catch (Exception e) {
	                throw new IOException("Invalid recurring value format");
	            }

	            Expenditure expenditure = new Expenditure(date, amount, description, category, recurring);
	            newExpenditures.add(expenditure);
	        }
	    }

	    // Set user's expenditures to the imported expenditures
	    user.setExpenditures(newExpenditures);
	}

}
