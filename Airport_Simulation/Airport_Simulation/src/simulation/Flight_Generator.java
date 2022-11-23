package simulation;

/* Flight_Generator Class having the generator method to generate the schedule for the fixed time period.
 * Input is the Normal probability distribution of the types of flights selected and sample size.  
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import java.lang.Math;
import gate_assignment.*;

public class Flight_Generator {
	static int emergency = 0;
	static List<Integer> temporaryFlightList = new ArrayList<Integer>();
	private static final Logger logger = Logger.getLogger(Flight_Generator.class.getName());
	private static double getRandomNumber(double lowerValue, double upperValue) {
		return ((Math.random() * (upperValue - lowerValue)) + lowerValue);
	}

	public static Scheduler scheduleAssignment(List<String> sequenceOfFlights, int flightsPerDay) {
		Scheduler assignedSchedule = new Scheduler();
		Map<String, Integer> miscTime = Map.of("a-", 10, "b-", 7, "c-", 4, "d-", 4, "em", 50);
		Map<Integer, Double> cushionVariable = new HashMap<>();
		cushionVariable.put(100, 29.0);
		cushionVariable.put(150, 19.3);
		cushionVariable.put(200, 14.5);
		cushionVariable.put(250, 11.6);
		cushionVariable.put(300, 9.68);
		cushionVariable.put(350, 8.3);
		cushionVariable.put(400, 7.2);
		cushionVariable.put(450, 6.45);
		cushionVariable.put(500, 5.8);
		cushionVariable.put(550, 5.28);
		cushionVariable.put(600, 4.82);
		cushionVariable.put(650, 4.47);
		cushionVariable.put(700, 4.15);
		cushionVariable.put(750, 3.85);
		cushionVariable.put(800, 3.61);
		cushionVariable.put(850, 3.4);
		cushionVariable.put(900, 3.2);
		cushionVariable.put(950, 3.09);
		cushionVariable.put(1000, 2.9);

		System.out.println("Sim Gen------------------------------");

		double startTime = 0, dayCount = 0, eta, etd;

		for (int i = 0; i < sequenceOfFlights.size(); i++) {
			startTime = startTime + getRandomNumber(0.0, cushionVariable.get(flightsPerDay));

			eta = 60 * startTime + getRandomNumber(1.0, 60.0);
			etd = 60 * (startTime + Flight.calculateTurnaroundTime((sequenceOfFlights.get(i)).substring(0, 2))
					+ ((miscTime.get((sequenceOfFlights.get(i)).substring(0, 2)) * getRandomNumber(0.0, 10.0))));

			assignedSchedule.addFlightInSchedule(new Flight(sequenceOfFlights.get(i), (int) eta, (int) etd));

			dayCount = etd;
			// emergency = 0;

		}
		System.out.println("Year count: " + dayCount / (float) (365 * 86400));
		assignedSchedule.setEmergencyCount(flightsPerDay * 3650 - emergency);
		emergency = 0;
		return assignedSchedule;
	}

	private static List<String> flightSequenceGenerator(List<Double> distributionResultList, int sampleSize) {

		char[] flightCategory;
		temporaryFlightList.clear();
		List<String> shuffledFlightList = new ArrayList<String>();

		flightCategory = new char[] { 'a', 'b', 'c', 'd' };

		for (int i = 0; i < distributionResultList.size(); i++) {
			int temp = (int) Math.round(distributionResultList.get(i) * sampleSize);
			temporaryFlightList.add(temp);
		}

		// Adding flights details i.e. Flight Type with Number
		for (int i = 0; i < distributionResultList.size(); i++) {
			emergency += temporaryFlightList.get(i);

			for (int j = 0; j < temporaryFlightList.get(i); j++) {
				shuffledFlightList.add(flightCategory[i] + "-" + String.valueOf(j + 1));
			}

		}

		// Adding the Emergency and High Priority Cases
		temporaryFlightList.add(sampleSize - emergency);
		// System.out.println("AreaGraph Shit" + (sampleSize - emergency));
		for (int i = 0; i < (sampleSize - emergency); i++) {
			shuffledFlightList.add("em-" + String.valueOf(i + 1));
		}

		// Random Sampling without Replacement to generate random schedule
		Collections.shuffle(shuffledFlightList);
		logger.info("Sequence of flights generated");
		return shuffledFlightList;
	}

	public static Scheduler generateDistribution(int flightType, int sampleSize) {

		// calling for normal distribution
		List<Double> distributionResultList = new ArrayList<Double>();
		distributionResultList = Probability_Distribution_Generator.generateNormalDistribution(flightType, sampleSize);

		// calling for generator
		List<String> sequenceOfFlights = new ArrayList<String>();
		sequenceOfFlights = flightSequenceGenerator(distributionResultList, sampleSize);
		
		// seq_flights = Flight_Generator.generate_distribution(4, i * 365 * 10);
		logger.info("Schedule for flights generated");
		return scheduleAssignment(sequenceOfFlights, sampleSize / (365 * 10));
	}

}
