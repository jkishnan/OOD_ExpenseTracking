package controller;

import java.io.File;
import javafx.scene.control.Label;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Budget;
import model.Expenditure;
import model.UpdatingTableCell;
import model.User;
import model.UserDatabase;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;


public class ExpenditureController {
	private Main main;

	public void setMain(Main main) {
	    this.main = main;
	}
	
	@FXML
	private TextField amountField;

	@FXML
	private TextField descriptionField;

	@FXML
	private TableView<Expenditure> expenditureTable;

	@FXML
	private TableColumn<Expenditure, LocalDate> dateColumn;

	@FXML
	private TableColumn<Expenditure, Double> amountColumn;

	@FXML
	private TableColumn<Expenditure, String> descriptionColumn;

	private ObservableList<Expenditure> expenditureData;

	@FXML
	private TableColumn<Expenditure, String> categoryColumn;

	@FXML
	private ComboBox<String> categoryComboBox;

	@FXML
	private DatePicker datePicker;

	@FXML
	private TextField searchField;
	private FilteredList<Expenditure> filteredExpenditures;

	private ObservableList<Expenditure> expenditures;

	@FXML
	private Button addButton;
	
	@FXML
	private Button importButton;

	@FXML
	private Button editButton;

	@FXML
	private Button editBudgetButton;
	
	@FXML
	private CheckBox recurringCheckBox;

	//Budgetting and Remaining Budget Allocation
	@FXML
	private TableView<Budget> budgetTable;

	@FXML
	private TableColumn<Budget, String> budgetCategoryColumn;

	@FXML
	private TableColumn<Budget, Double> budgetAmountColumn;

	@FXML
	private TableColumn<Budget, Double> budgetRemainingAmountColumn;
	
	//Visulaization and Pie Chart
	@FXML
	private AnchorPane pieChartPane;
	
	@FXML
	private AnchorPane expenditurePieChartPane;
	
	@FXML
	private Label totalExpenditureLabel;

	@FXML
	private Label remainingBudgetLabel;

	
	private UserDatabase userDatabase;
	private User user;



	// Define a list of categories
	private ObservableList<String> categories = FXCollections.observableArrayList("Rent", "Groceries", "Utilities",
			"Entertainment", "Transportation", "Others");

	private ObservableList<Budget> budgets = FXCollections.observableArrayList();
	private ObservableList<Budget> remainingBudgets = FXCollections.observableArrayList();

	@FXML
	public void initialize() {
		userDatabase = UserDatabase.getInstance();
		user = User.getInstance();
		expenditureData = FXCollections.observableArrayList();
		expenditureTable.setItems(expenditureData);

		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

		dateColumn.setSortable(true);
		amountColumn.setSortable(true);

		// Sorting
		dateColumn.setComparator(dateColumn.getComparator().reversed());
		categoryColumn.setComparator(Comparator.naturalOrder());
		descriptionColumn.setComparator(Comparator.naturalOrder());
		amountColumn.setComparator(Comparator.naturalOrder());

		expenditureTable.getSortOrder().addAll(dateColumn, categoryColumn, descriptionColumn, amountColumn);

		expenditures = FXCollections.observableArrayList();
		expenditureTable.setItems(expenditures);
		categoryComboBox.setItems(categories);

		addButton.setOnAction(event -> addExpenditure());
		importButton.setOnAction(event -> handleImport());


		filteredExpenditures = new FilteredList<>(expenditures, p -> true);
		expenditureTable.setItems(filteredExpenditures);
		
		updateExpenditurePieChart();

		// Budgetting
		budgets.add(new Budget("Rent", 1000.00));
		budgets.add(new Budget("Groceries", 200.00));
		budgets.add(new Budget("Utilities", 150.00));
		budgets.add(new Budget("Entertainment", 100.00));
		budgets.add(new Budget("Transportation", 50.00));
		budgets.add(new Budget("Others", 50.00));

		budgetTable.setItems(budgets);

		budgetCategoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
		budgetAmountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
		budgetRemainingAmountColumn
				.setCellValueFactory(cellData -> cellData.getValue().remainingAmountProperty().asObject());
		budgetRemainingAmountColumn.setCellFactory(column -> new UpdatingTableCell());

		for (Budget budget : budgets) {
		    remainingBudgets.add(new Budget(budget.getCategory(), budget.getAmount()));
		}
		updatePieChart();
		updateLabels();
	}

	@FXML
	private void addExpenditure() {
		LocalDate date = datePicker.getValue();
		String description = descriptionField.getText();
		String amountText = amountField.getText();
		String category = categoryComboBox.getSelectionModel().getSelectedItem();
		boolean recurring = recurringCheckBox.isSelected();

		if (date == null || description.isEmpty() || amountText.isEmpty() || category == null) {
			showErrorAlert("Please enter valid expense information.");
			return;
		}

		double amount;
		try {
			amount = Double.parseDouble(amountText);
		} catch (NumberFormatException e) {
			showErrorAlert("Invalid amount entered.");
			return;
		}

		Expenditure expenditure = new Expenditure(date, amount, description, category, recurring); 
		expenditures.add(expenditure);
		updateRemainingBudget(expenditure);
		updateLabels();
		updateExpenditurePieChart();
		user.setExpenditures(expenditures);
		clearForm();
	}

	private void showErrorAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void clearForm() {
		datePicker.setValue(null);
		descriptionField.clear();
		amountField.clear();
		categoryComboBox.getSelectionModel().clearSelection();
	}

	@FXML
	private void searchExpenditures() {
		String searchQuery = searchField.getText().toLowerCase();
		filteredExpenditures.setPredicate(expenditure -> {
			if (searchQuery == null || searchQuery.isEmpty()) {
				return true;
			}
			String description = expenditure.getDescription().toLowerCase();
			String category = expenditure.getCategory().toLowerCase();
			return description.contains(searchQuery) || category.contains(searchQuery);
		});
	}

	@FXML
	private void editExpenditure() {
		Expenditure selectedExpenditure = expenditureTable.getSelectionModel().getSelectedItem();
		if (selectedExpenditure == null) {
			showErrorAlert("Please select an expense to edit.");
			return;
		}
		
		// Store the original expenditure details
	    Expenditure originalExpenditure = new Expenditure(selectedExpenditure);

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/EditExpenditure.fxml"));
			Parent editDialogRoot = fxmlLoader.load();
			EditExpenditureController editExpenditureController = fxmlLoader.getController();
			editExpenditureController.setExpenditure(selectedExpenditure);
			editExpenditureController.setCategories(categories);

			Stage editDialogStage = new Stage();
			editDialogStage.setTitle("Edit Expense");
			editDialogStage.initModality(Modality.WINDOW_MODAL);
			editDialogStage.initOwner(expenditureTable.getScene().getWindow());
			editDialogStage.setScene(new Scene(editDialogRoot));

			editDialogStage.setOnHidden(event -> {
				if (editExpenditureController.isSaveClicked()) {
					Expenditure updatedExpenditure = editExpenditureController.getUpdatedExpenditure();
					selectedExpenditure.setDate(updatedExpenditure.getDate());
					selectedExpenditure.setDescription(updatedExpenditure.getDescription());
					selectedExpenditure.setAmount(updatedExpenditure.getAmount());
					selectedExpenditure.setCategory(updatedExpenditure.getCategory());
					
					expenditureTable.refresh();
					
					// Adjust the remaining budget after editing
	                updateRemainingBudgetAfterEditing(originalExpenditure, selectedExpenditure);
	                updateLabels();
	                updatePieChart();
	                updateExpenditurePieChart();
				}
			});

			editDialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			showErrorAlert("Error occurred while opening the edit dialog.");
		}
	}
	
	private void updateRemainingBudgetAfterEditing(Expenditure originalExpenditure, Expenditure updatedExpenditure) {
	    // Return the original amount to the budget if the category has changed or if the amount has changed
	    if (!originalExpenditure.getCategory().equals(updatedExpenditure.getCategory()) || originalExpenditure.getAmount() != updatedExpenditure.getAmount()) {
	        for (Budget budget : budgets) {
	            if (budget.getCategory().equals(originalExpenditure.getCategory())) {
	                budget.setRemainingAmount(budget.getRemainingAmount() + originalExpenditure.getAmount());
	                break;
	            }
	        }
	    }

	    // Subtract the updated amount from the budget
	    for (Budget budget : budgets) {
	        if (budget.getCategory().equals(updatedExpenditure.getCategory())) {
	            budget.setRemainingAmount(budget.getRemainingAmount() - updatedExpenditure.getAmount());
	            break;
	        }
	    }
	}


	private void updateRemainingBudget(Expenditure expenditure) {
		for (Budget budget : budgets) {
			if (budget.getCategory().equals(expenditure.getCategory())) {
				budget.setRemainingAmount(budget.getRemainingAmount() - expenditure.getAmount());
				break;
			}
		}
		updatePieChart();
	}
	
	private void updatePieChart() {
	    PieChart pieChart = new PieChart();
	    pieChart.setTitle("Remaining Budget");
	    
	    for (Budget budget : budgets) {
	        PieChart.Data slice = new PieChart.Data(budget.getCategory(), budget.getRemainingAmount());
	        pieChart.getData().add(slice);
	    }
	    
	    pieChartPane.getChildren().setAll(pieChart);
	}
	
	private void updateLabels() {
	    double totalExpenditure = expenditures.stream().mapToDouble(Expenditure::getAmount).sum();
	    totalExpenditureLabel.setText(String.format("Total Expenditure: %.2f", totalExpenditure));

	    double remainingBudget = budgets.stream().mapToDouble(Budget::getRemainingAmount).sum();
	    remainingBudgetLabel.setText(String.format("Remaining Budget: %.2f", remainingBudget));
	}

	
	private void updateExpenditurePieChart() {
	    PieChart expenditurePieChart = new PieChart();
	    expenditurePieChart.setTitle("Expenditure by Category");
	    
	    // Calculate total expenditure for each category
	    Map<String, Double> expenditureByCategory = new HashMap<>();
	    for (Expenditure expenditure : expenditures) {
	        String category = expenditure.getCategory();
	        double amount = expenditure.getAmount();
	        expenditureByCategory.put(category, expenditureByCategory.getOrDefault(category, 0.0) + amount);
	    }

	    // Create pie chart slices for each category
	    for (Map.Entry<String, Double> entry : expenditureByCategory.entrySet()) {
	        PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
	        expenditurePieChart.getData().add(slice);
	    }
	    
	    expenditurePieChartPane.getChildren().setAll(expenditurePieChart);
	}
	
	@FXML
	public void handleExport() {
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Export Expenditure Data");
	    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
	    File file = fileChooser.showSaveDialog(main.getPrimaryStage());
	    if (file != null) {
	        try {
	            userDatabase.exportExpenditures(user,file);
	        } catch (IOException e) {
	            showErrorAlert("Error exporting data: " + e.getMessage());
	        }
	    }
	}
	
	@FXML
	public void handleImport() {
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Import Expenditure Data");
	    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
	    File file = fileChooser.showOpenDialog(main.getPrimaryStage());
	    if (file != null) {
	        try {
	            userDatabase.importExpenditures(user, file);
	            populateExpenditureTable();
	            updateExpenditurePieChart();
	        } catch (IOException e) {
	            showErrorAlert("Error importing data: " + e.getMessage());
	        }
	    }
	}
	
	private void populateExpenditureTable() {
	    // Clear the current expenditure list
	    //expenditures.clear();

	    // Get the updated expenditure list from the user
	    List<Expenditure> userExpenditures = user.getExpenditures();

	    // Add the user's expenditures to the table
	    for (Expenditure expenditure : userExpenditures) {
	        expenditures.add(expenditure);
	    }
	}
}
