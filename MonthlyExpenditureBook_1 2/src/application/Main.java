package application;

import java.io.IOException;

import controller.ExpenditureController;
import controller.LoginController;
import controller.SignupController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {

	private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLogin();
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/Login.fxml"));
            Pane loginPane = loader.load();
            LoginController loginController = loader.getController();
            loginController.setMain(this);
            
            loginPane.setPrefWidth(400);
            loginPane.setPrefHeight(300);

            primaryStage.setScene(new Scene(loginPane));
            primaryStage.setTitle("Login");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showMainApplication() {
    	try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/ExpenditureView.fxml"));
            Pane expenditureLayout = loader.load();
            ExpenditureController expenditureController = loader.getController();
            expenditureController.setMain(this);
            Scene scene = new Scene(expenditureLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Monthly Expenditure Book");
            primaryStage.setFullScreen(true);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void showSignupWindow() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/Signup.fxml"));
            Parent root = loader.load();

            Stage signupStage = new Stage();
            signupStage.setTitle("Sign Up");
            signupStage.initModality(Modality.WINDOW_MODAL);
            signupStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            signupStage.setScene(scene);

            SignupController signUpController = loader.getController();
            signUpController.setMain(this);
            signupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showExpenditureWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/ExpenditureView.fxml"));
            BorderPane expenditureLayout = (BorderPane) loader.load();
            ExpenditureController expenditureController = loader.getController();
            expenditureController.setMain(this);
            Scene scene = new Scene(expenditureLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Monthly Expenditure Book");
            primaryStage.setFullScreen(true);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
