package controller;

import model.UserDatabase;
import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignupController {
	
	private Main main;

	public void setMain(Main main) {
	    this.main = main;
	}

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button signUpButton;

    private UserDatabase userDatabase;

    public void initialize() {
        userDatabase = UserDatabase.getInstance();
        signUpButton.setOnAction(event -> handleSignUp(event));
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showErrorAlert("Username and password cannot be empty.");
            return;
        }

        boolean signUpSuccess = userDatabase.signUp(username, password);
        if (signUpSuccess) {
            showAlert(Alert.AlertType.INFORMATION, "Sign Up Successful", "You have successfully signed up.");
            closeSignUpWindow();
            main.showMainApplication();   
        } else {
            showErrorAlert("Username already exists. Please choose a different username.");
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

    private void closeSignUpWindow() {
        Stage stage = (Stage) signUpButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void handleCancel() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
