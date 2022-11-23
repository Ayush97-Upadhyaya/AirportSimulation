package gate_assignment;

import java.util.ArrayList;

public interface Scheduler_Interface {
	void addFlightInSchedule(Flight currentFlight);

	int numberOfGatesRequired();

	ArrayList<Flight> getSchedule();
}
