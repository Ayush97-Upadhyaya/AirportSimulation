package gate_assignment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import org.apache.log4j.Logger;

import gui.FrontEnd;

/*
 * Scheduler class
 */

class PriorityQueueComparator implements Comparator<Flight> {
	public int compare(Flight f1, Flight f2) {
		if (f1.getETD() > f2.getETD()) {
			return 1;
		} else {
			return 0;
		}
	}
}

public class Scheduler implements Scheduler_Interface {

	private ArrayList<Flight> flightSchedule;
	private PriorityQueue<Flight> flightScheduleArrangedByETD;
	private int numberOfGatesRequiredValue;
	private int emergencyCount;
	private static int newGateNumber;
	private static double[] activeGatesByDay = new double[4000];
	
	static ArrayList<Gate> activeGates;
	static ArrayList<Gate> inActiveGates;
	private static final Logger logger = Logger.getLogger(FrontEnd.class.getName());
	
	public Scheduler() {
		for (int i = 0; i < 3650; i++) {
			activeGatesByDay[i] = -1;
		}
		emergencyCount = 0;

		flightSchedule = new ArrayList<Flight>();

		numberOfGatesRequiredValue = 0;

		activeGates = new ArrayList<>();
		inActiveGates = new ArrayList<>();
		newGateNumber=0;
		flightScheduleArrangedByETD = new PriorityQueue<Flight>(new PriorityQueueComparator());
	}

	// ----------------------------methods---------------------------------//
	public void addFlightInSchedule(Flight currentFlight) {	flightSchedule.add(currentFlight);	}
	
	protected static void addInFlightsAssignedToGate(Flight currentFlight, int cnt) {
		/* No gate is available to assign the current flight then increase the gate
		 * number by 1 Assign this new gate to the current flight*/
		
		if (inActiveGates.isEmpty()){
			newGateNumber += 1;
			Gate newGate = new Gate(newGateNumber, currentFlight);
			activeGates.add(newGate);
			currentFlight.setGateNumber(newGateNumber);
			//Add sql query
		}	else {
			Gate freeGate = inActiveGates.get(0);
			freeGate.addFlightInGate(currentFlight);
			freeGate.setActive();
			currentFlight.setGateNumber(freeGate.getGateNumber());
			//Add sql query
			inActiveGates.remove(0);
			activeGates.add(freeGate);
		}
		activeGatesByDay[cnt - 1] = (double) activeGates.size();
	}

	public ArrayList<Flight> getSchedule() {	return flightSchedule;	}

	public int numberOfGatesRequired() {
		/* Generate schedule and assign the gate numbers to flights Returns the number
		 * of gates required to accommodate the schedule*/
//		logger.info("Calculating number of gates required");
		flightScheduleArrangedByETD.add(flightSchedule.get(0));
		flightSchedule.get(0).setGateNumber(0);
		Gate newGate = new Gate(0,flightSchedule.get(0));
		activeGates.add(newGate);
		
		int sizeOfflightSchedule = flightSchedule.size();
		int dayCount = 1;
		for (int i = 1; i < sizeOfflightSchedule; i++) {
			Flight currentFlightInSchedule = flightSchedule.get(i);
			if (currentFlightInSchedule.getETA() > (86400 * dayCount)) {
				dayCount = dayCount + 1;
			}
			flightScheduleArrangedByETD.add(currentFlightInSchedule);
			while (!flightScheduleArrangedByETD.isEmpty()
					&& currentFlightInSchedule.getETA() >= flightScheduleArrangedByETD.peek().getETD()) {
				Flight f = flightScheduleArrangedByETD.poll();
				int tempGateNumber = f.getGateNumber();
				freeGates(tempGateNumber);				
			}
			addInFlightsAssignedToGate(currentFlightInSchedule, dayCount);
		}
		numberOfGatesRequiredValue = (activeGates.size()+inActiveGates.size());
		logger.info("Gate assignment completed");
		return numberOfGatesRequiredValue;
	}
	
	private void freeGates(int tempGateNumber) {
		int tempSize = activeGates.size();
		for(int j = 0 ; j< tempSize; j++) {
			Gate currentGate = activeGates.get(j);
			int temp = currentGate.getGateNumber();
			if(temp  == tempGateNumber ) {
				activeGates.remove(j);
				currentGate.setInActive();
				inActiveGates.add(currentGate);
				tempSize--;
				j--;
			}
		}
	}
	
	public int getNumberOfGatesRequired() {
		return numberOfGatesRequiredValue;
	}

	public void setEmergencyCount(int count) {
		this.emergencyCount = count;
	}

	public double getEmergencyCount() {
		// TODO Auto-generated method stub
		return emergencyCount;
	}

	public double[] getGatesByDayCount() {
		// TODO Auto-generated method stub
		return activeGatesByDay;
	}
}
