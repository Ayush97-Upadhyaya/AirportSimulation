package simulation;

import org.apache.log4j.Logger;

import gate_assignment.*;
import gui.FrontEnd;

public class Driver {
	
	private static final Logger logger = Logger.getLogger(Driver.class.getName());
	
	public static double[][] numberOfGatesUsed = new double[19][10];
	public static double[] numberOfEmergency = new double[19];
	public static double[][] numberGatesByDay = new double[19][3650];

	public static String program_driver(int guiMinInput, int guiMaxInput) {
		logger.info("Executing program driver");
		StringBuilder guiOutput = new StringBuilder();
		guiOutput.append("");
		System.out.println(guiMinInput + " " + guiMaxInput);
		int index = 0;
		for (int i = guiMinInput; i <= guiMaxInput; i = i + 50) {
			int minimum = 100000, maximum = -100000;
			logger.info("Running Execution for " + i + " flights per day");
			for (int j = 0; j < 10; j++) {
				
				Scheduler scheduler = new Scheduler();
				scheduler = Flight_Generator.generateDistribution(4, i * 365 * 10);

				int number_of_gates = scheduler.numberOfGatesRequired();
				if (number_of_gates < 100 && number_of_gates > maximum) {
					maximum = number_of_gates;
					double temp[] = new double[3650];
					temp = scheduler.getGatesByDayCount();
					for (int k = 0; k < 3650; k++) {	numberGatesByDay[index][k] = temp[k];	}
				}
				if (number_of_gates < minimum) {
					minimum = number_of_gates;
				}
				System.out.println(i + " " + number_of_gates);
				numberOfGatesUsed[index][j] = number_of_gates;
				numberOfEmergency[index] = Flight_Generator.temporaryFlightList.get(4);
			}
			index += 1;
			guiOutput.append(" •) " + i + " flights per day: " + minimum + " to " + maximum + "\n");
			logger.info( i + " flights per day resulted number of gates ranging from" +  minimum + " to " + maximum + "\n");
		}
		Runtime.getRuntime().gc();
		return guiOutput.toString();
	}

}
