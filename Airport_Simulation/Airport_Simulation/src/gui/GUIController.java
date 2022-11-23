package gui;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import simulation.Driver;

public class GUIController implements Initializable {
	private static final Logger logger = Logger.getLogger(GUIController.class.getName());

	public void alertHandler(String s) {
		Label secondLabel = new Label(s);
		secondLabel.setWrapText(true);
		StackPane secondaryLayout = new StackPane();
		secondaryLayout.getChildren().add(secondLabel);

		Scene secondScene = new Scene(secondaryLayout, 280, 100);

// New window (Stage)
		Stage newWindow = new Stage();
		newWindow.setTitle("Alert!");
		newWindow.setScene(secondScene);

		newWindow.show();
	}

	public void newWindowHandler(int numOfCharts, double[][] dataForGraph, int minSizeX, int loopSize, int step,
			String title, String xlabel, int lowerLegendValue) {

		Stage newWindow = new Stage();
		newWindow.setTitle("Airport Simulation");
// defining the axes
		final NumberAxis xAxis = new NumberAxis(1 - (0.1 * step), loopSize + (0.1 * step), step);
		final NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Number of Gates");
		xAxis.setLabel(xlabel);
// creating the chart
		final AreaChart<Number, Number> lineChart = new AreaChart<>(xAxis, yAxis);

		lineChart.setTitle(title);
		lineChart.setLegendSide(Side.LEFT);
// defining a series
		for (int i = numOfCharts - 1; i >= 0; i--) {
			XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
			series.setName(Integer.toString((lowerLegendValue + (50 * i))));
			for (int j = 0; j < loopSize; j++) {
				final Data<Number, Number> data = new XYChart.Data<>(j + 1, (int) dataForGraph[i][j]);
				data.setNode(new HoveredThresholdNode(j + 1, (int) dataForGraph[i][j]));
				series.getData().add(data);
// lineChart.getData().add(series);
			}
			lineChart.getData().add(series);
		}
		ScrollPane root = new ScrollPane(lineChart);
		root.setMinSize(minSizeX, 600);
		lineChart.setMinSize(root.getMinWidth(), root.getMinHeight() - 20);
		Scene scene = new Scene(root, 800, 600);
// lineChart.getData().addAll(series,series1);

		newWindow.setScene(scene);

		newWindow.show();

	}

	int count = 0;
	@FXML
	private Label label1, label2;
	private int lowerBound, upperBound;
	public ComboBox<String> combobox, combobox1;
	ObservableList<String> listt = FXCollections.observableArrayList("100", "200", "300", "400", "500", "600", "700",
			"800", "900", "1000");

	@Override
	public void initialize(URL location, ResourceBundle resources) {
// TODO Auto-generated method stub
		combobox.setItems(listt);
		combobox1.setItems(listt);
	}

	@FXML
	public void calculateHandler(ActionEvent event) {
		if (combobox.getValue() != null && combobox1.getValue() != null) {
			lowerBound = Integer.parseInt((combobox.getValue()));
			upperBound = Integer.parseInt((combobox1.getValue()));
			String output = new String();
			if (lowerBound <= upperBound) {
				logger.info("Simulation Started for range: " + Integer.toString(lowerBound) + " to "
						+ Integer.toString(upperBound));
				output = Driver.program_driver(lowerBound, upperBound);
				logger.info("Simulation Completed");
				label1.setText(output);
			} else {
				logger.error("Minimum value greater than maximum value.");
				alertHandler("Minimum value greater than maximum value.");
			}
		} else {
			logger.error("Invalid Input: Value not in range");
			alertHandler("Please Enter Value.");
		}
	}

	@FXML
	public void handlingHandler(ActionEvent event) {
		if (combobox.getValue() != null && combobox1.getValue() != null) {
			lowerBound = Integer.parseInt((combobox.getValue()));
			upperBound = Integer.parseInt((combobox1.getValue()));
			logger.info("High Priority Landings data requested");
			int index = 0;
			StringBuilder sb = new StringBuilder("High Priority Handlings:\n");

			double[] numberOfEmergency = Driver.numberOfEmergency;
			if (numberOfEmergency[0] != 0.0) {
				for (int i = lowerBound; i <= upperBound; i += 50) {
					double[] emergency = numberOfEmergency;
// System.out.println(emergency);
					sb.append("  •) " + i + " flights: " + (int) emergency[index] + "\n");
					index += 1;
				}
				label2.setText(sb.toString());
				logger.info("High Priority landings Displayed");
			}
			else {
				logger.error("Number of emergency not recieved from driver.");
				alertHandler("Please enter appropriate input.");
			}
		} else {
			logger.error("Invalid Input: Value not in range");
			alertHandler("Please Enter Value");
		}
	}

	@FXML
	public void graphHandler1(ActionEvent event) {
		if (combobox.getValue() != null && combobox1.getValue() != null) {
			logger.info("Request for Minimum Number of Gates graph submitted");
			lowerBound = Integer.parseInt((combobox.getValue()));
			upperBound = Integer.parseInt((combobox1.getValue()));

			int numberOfCharts = (upperBound - lowerBound) / 50 + 1;

			double[][] numberOfGatesUsed = Driver.numberOfGatesUsed;
			// System.out.println(numberOfGatesUsed[0][0]);
			if (numberOfGatesUsed[0][0] != 0.0) {
				newWindowHandler(numberOfCharts, numberOfGatesUsed, 800, 10, 1, "Minimum Gate per Simulation",
						"Iteration Number", lowerBound);
				logger.info("Minimum Number of Gates graph generated");
			} else {
				logger.error("Can't retreive number of gates value from driver.");
				alertHandler("Please enter appropriate Input");
			}
		} else {
			logger.error("Error generating Minimum Number of Gates graph");
			alertHandler("Please Enter Value.");
		}
	}

	@FXML
	public void graphHandler2(ActionEvent event) {

		if (combobox.getValue() != null && combobox1.getValue() != null) {
			logger.info("Request for Congestion graph submitted");
			lowerBound = Integer.parseInt((combobox.getValue()));
			upperBound = Integer.parseInt((combobox1.getValue()));

			int numberOfCharts = (upperBound - lowerBound) / 50 + 1;

			double[][] numberGatesByDay = Driver.numberGatesByDay;
			if (numberGatesByDay[0][0] != 0.0) {
				newWindowHandler(numberOfCharts, numberGatesByDay, 50000, 3650, 10, "Gate Usage", "Day Number",
						lowerBound);
				logger.info("Congestion graph generated");
			} else {
				logger.error("Can't retreive number of gates by day from driver.");
				alertHandler("Please enter appropriate input.");
			}

		} else {
			logger.error("Error generating Congestion graph");
			alertHandler("Please Enter Value.");
		}
	}

	@FXML
	public void hintHandler(ActionEvent event) {
		Node source = (Node) event.getSource();
		Window theStage = source.getScene().getWindow();
		if (count % 2 == 0) {
			FrontEnd.popup.show(theStage);
			count++;
		} else if (count % 2 == 1) {
			FrontEnd.popup.hide();
			count++;
		}
		// System.out.println(count);
	}

	@FXML
	public void refreshHandler(ActionEvent event) {
		logger.info("Refresh Button execution started");
		label1.setText("");
		label2.setText("");
		combobox.valueProperty().setValue(null);
		combobox1.valueProperty().setValue(null);
	}
}