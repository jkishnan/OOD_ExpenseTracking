package model;

import javafx.beans.binding.Bindings;
import javafx.scene.control.TableCell;

public class UpdatingTableCell extends TableCell<Budget, Double> {
    @Override
    protected void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            textProperty().unbind();
            setText(null);
            setGraphic(null);
        } else {
            textProperty().unbind();
            textProperty().bind(Bindings.format("%.2f", item));
        }
    }
}
