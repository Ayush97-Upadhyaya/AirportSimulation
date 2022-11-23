package gui;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/** a node which displays a value on hover, but is otherwise empty */
class HoveredThresholdNode extends StackPane {
	HoveredThresholdNode(int priorValue, int gatesUsed) {
		setPrefSize(10, 10);

		final Label label = createDataThresholdLabel(priorValue, gatesUsed);

		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				getChildren().setAll(label);
				setCursor(Cursor.NONE);
				toFront();
			}
		});
		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				getChildren().clear();
				setCursor(Cursor.CROSSHAIR);
			}
		});
	}

	private Label createDataThresholdLabel(int priorValue, int gatesUsed) {
		final Label label = new Label(priorValue + "," + gatesUsed + "");
		label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
		label.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

		if (priorValue == 0) {
			label.setTextFill(Color.DARKGRAY);
		} else if (gatesUsed > priorValue) {
			label.setTextFill(Color.FORESTGREEN);
		} else {
			label.setTextFill(Color.FIREBRICK);
		}

		label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
		return label;
	}
}