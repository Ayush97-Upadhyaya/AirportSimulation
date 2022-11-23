package gate_assignment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HsqlDBConnection {
	public static void insertSchedule(int id, int simulationNumber, String flightNumber,int gateNumber, int eta, int etd) {
		Connection conn = null;
		String db = "jdbc:hsqldb:file:C:\\Users\\ah7anb\\Downloads\\Airport_Simulation_Presentation-20190802T104433Z-001\\Database\\AirportData;ifexists=true";
		String user = "SA";
		String password = "";
		

		try {
			// Create database connection
			conn = DriverManager.getConnection(db, user, password);
			// Create and execute statement
			Statement stmt = conn.createStatement();
			//Flight Number is appended with simulation number before inserting into database.
			flightNumber = flightNumber + Integer.toString(simulationNumber);
			ResultSet rs = stmt.executeQuery("INSERT INTO schedule (ID, flightNumber,gateNumber, eta, etd)\r\n" + 
					"VALUES ("+id+", "+flightNumber+", "+gateNumber+", "+eta+", "+etd+" );");

			
			// Clean up
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				// Close connection
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
	}
	public static void retriveSchedule(int id) {
		Connection conn = null;
		String db = "jdbc:hsqldb:file:C:\\Users\\ah7anb\\Downloads\\Airport_Simulation_Presentation-20190802T104433Z-001\\Database\\AirportData;ifexists=true";
		String user = "SA";
		String password = "";
		

		try {
			// Create database connection
			conn = DriverManager.getConnection(db, user, password);
			// Create and execute statement
			Statement stmt = conn.createStatement();
			ResultSet retrivRs = stmt.executeQuery("SELECT * FROM schedule WHERE id=");

			
			// Clean up
			retrivRs.close();
			stmt.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				// Close connection
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
	}
}
