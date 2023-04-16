module MonthlyExpenditureBook_1 {
	
	
	    requires javafx.controls;
	    requires javafx.fxml;
		requires javafx.graphics;
		requires javafx.base;
		requires com.fasterxml.jackson.databind;
		requires com.fasterxml.jackson.datatype.jsr310;

	    exports controller;
	    exports model to com.fasterxml.jackson.databind;

	    opens controller to javafx.fxml;
		opens application to javafx.graphics, javafx.fxml;
		opens model to javafx.base;

}
