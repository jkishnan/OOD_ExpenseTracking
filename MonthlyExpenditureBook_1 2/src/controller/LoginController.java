package controller;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;
import model.UserDatabase;

public class LoginController {
	
	
	private Main main;

	public void setMain(Main main) {
	    this.main = main;
	}
	
	@FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text loginErrorText;
    
    @FXML
    private Button loginButton;

    private UserDatabase userDatabase;

    
    public void initialize() {
        userDatabase = UserDatabase.getInstance();
        loginButton.setOnAction(event -> handleLogin(event));
    }


    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (userDatabase.authenticate(username, password)) {
        	showAlert(Alert.AlertType.INFORMATION, "Login Succesful","Welcome to Monthly Expenditure Book");
            main.showMainApplication();
        } else {
        	showErrorAlert("Invalid User Id/Password");
        }
    }
    
    private void showErrorAlert(String message) {
        showAlert(Alert.AlertType.ERROR, "Error", message);
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void showSignup() {
    	main.showSignupWindow();
    }
}
