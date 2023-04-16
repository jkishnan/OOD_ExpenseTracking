package controller;

import java.time.LocalDate;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Expenditure;

public class EditExpenditureController {

    @FXML
    private TextField amountField;

    @FXML
    private TextField descriptionField;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private DatePicker datePicker;

    private Expenditure expenditure;
    
    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private boolean saveClicked = false;
    private ObservableList<String> categories;

    public void setCategories(ObservableList<String> categories) {
        this.categories = categories;
        categoryComboBox.setItems(categories);
    }

    public void setExpenditure(Expenditure expenditure) {
        this.expenditure = expenditure;

        // Initialize the form fields with the current expenditure values
        datePicker.setValue(expenditure.getDate());
        descriptionField.setText(expenditure.getDescription());
        amountField.setText(String.valueOf(expenditure.getAmount()));
        categoryComboBox.setValue(expenditure.getCategory());
    }
    
    public boolean isSaveClicked() {
        return saveClicked;
    }

    public Expenditure getUpdatedExpenditure() {
        LocalDate date = datePicker.getValue();
        String description = descriptionField.getText();
        String amountText = amountField.getText();
        String category = categoryComboBox.getSelectionModel().getSelectedItem();

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch(NumberFormatException e) {
        	showErrorAlert("Invalid amount entered.");
        	return null;
        	
        }
        return new Expenditure(date, amount, description, category);
    }
    
    @FXML
    private void saveExpenditure() {
        LocalDate date = datePicker.getValue();
        String description = descriptionField.getText();
        String amountText = amountField.getText();
        String category = categoryComboBox.getSelectionModel().getSelectedItem();

        if (date == null || description.isEmpty() || amountText.isEmpty() || category == null) {
            showErrorAlert("Please enter valid expense information.");
            return;
        }

        saveClicked = true;
        closeDialog();
    }

    @FXML
    private void closeDialog() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    
}
