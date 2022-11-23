module airport_simulation {
	requires java.desktop;
	requires javafx.graphics;
	requires javafx.controls;
	requires javafx.base;
	requires javafx.fxml;
	requires opencsv;
	requires java.sql;
	requires log4j;
//	requires log4j;
	exports gui;
	opens gui to javafx.fxml;
}