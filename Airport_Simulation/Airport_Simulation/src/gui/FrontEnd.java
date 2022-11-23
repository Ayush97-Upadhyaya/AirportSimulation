package gui;
	
import org.apache.log4j.Logger;


import gate_assignment.Flight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class FrontEnd extends Application {
	
	private static final Logger logger = Logger.getLogger(FrontEnd.class.getName());
	
	final static Popup popup = new Popup();
	@Override
	public void start(Stage primaryStage) {
		try {
			logger.info("Executing GUI");
			Parent root = FXMLLoader.load(getClass().getResource("/gui/Main.fxml"));
			Scene scene = new Scene(root,600,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		    popup.setX(300);
		    popup.setY(200);
		    Rectangle rect = new Rectangle(300, 400);
		    rect.setFill(Color.web("B7D2FB"));
		    rect.setArcHeight(20);
		    rect.setArcWidth(20);
		    final Text text = new Text (" •) The input to the simulation is the range of average number of flights per day that the airport has to handle. The output is the range of gates required to the input flights optimally.\n" + 
		    	   	"\n" + 
		    	   	" •) The 'Gate Data' button shows a graph of maximum gates used per instance of the simulation.\n" +
		    	   	"\n" +
		    	   	" •) The 'Peak Analysis' button gives the congestion visualisation. The 'Handlings' button gives the maximum number of high priority flights handled throughout the instances of simulation.\n" +
		    	   	"\n" + 
		    	   	" •) To change the refuel rate, fuel capacity change Parameters.csv file available at\n path: src/resources/Assets/Input Data\n" + 
		    	   	"\n" +
		    	   	" •) To add more parameters specify thier minimum and maximum value in minutes in seperate columns. These parameters if added must be specified after the parameters specified and before TURNAROUND_TIME_MAX.");
		    text.setWrappingWidth(280);
		    text.setTextAlignment(TextAlignment.JUSTIFY);
		    final StackPane stack = new StackPane();
		    stack.getChildren().addAll(rect, text);
		    stack.setLayoutX(760);
		    stack.setLayoutY(0);
		    popup.getContent().addAll(stack);
		    primaryStage.setTitle("Airport Simulator");
		    primaryStage.getIcons().add(new Image("file:"+"src\\resources\\Assets\\Images\\plane.png"));
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			logger.error("GUI cannot be initialised. Error message : " +e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		logger.info("Initialising application");
		Flight.readFile();
		launch(args);
	}
}
