package gate_assignment;

import java.util.ArrayList;

public class Gate {

	int gateNumber;
	ArrayList<Flight> sequenceOfFlight;
	boolean isActive;
	
	public Gate(int gateNumber,Flight flight) {
		isActive = true;
		this.gateNumber = gateNumber;
		sequenceOfFlight = new ArrayList<>();
		sequenceOfFlight.add(flight);
	}
	
	public boolean isGateActive() {	return isActive;	}
	
	public void addFlightInGate(Flight newFlight) {	sequenceOfFlight.add(newFlight);	}
	
	public int getGateNumber() {	return gateNumber;	}
	public void setActive() {	isActive=true;	}
	public void setInActive() {	isActive=false;	}
}