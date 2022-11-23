package gate_assignment;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.opencsv.CSVReader;

import gui.FrontEnd;


/*
* Flight class contains the characteristics of a single flight for a certain schedule
* like
* Estimated Time of Arrival(ETA), Estimated Time of Departure(ETD), Flight Number(flightNumber) and
* the alloted Boarding Gate number
* 
 */

public class Flight {
	
	private static final Logger logger = Logger.getLogger(Flight.class.getName());
	private int ETA;
	private int ETD;
	private int gateNumber;
	private String flightNumber;

	private static final ArrayList<String> typesOfParameters = new ArrayList<>();
	private static final Map<String,Map<String,Integer>> descriptionOfFlightData=new HashMap<String,Map<String,Integer>>();
	
	// Constructor
	public Flight(String fl_no, int eta, int etd) {
		flightNumber = fl_no;
		ETA = eta;
		ETD = etd;
	}

	//Read the file with all the different parameters and their values associated with different types of flights
	public static void readFile() 
	{ 
//		 file format should be [flightCategory,FUEL_CAPACITY_MIN,FUEL_CAPACITY_MAX,REFUEL_RATE_MIN, REFUEL_RATE_MAX, 
//		...variables with min and max rage defined, TURNAROUND_TIME_MAX ]
		String file ="src\\resources\\Assets\\Input Data\\Parameters.csv"; 
		try { 
			logger.info("Reading Parameters.csv file for flight data");
            FileReader filereader = new FileReader(file); 
  
            CSVReader csvReader = new CSVReader(filereader); 
            String[] nextRecord; 
            nextRecord = csvReader.readNext();
            
            for (String cell : nextRecord) {
            	typesOfParameters.add(cell);
//            	System.out.println(cell);
            }
            
            while ((nextRecord = csvReader.readNext()) != null) { 
            	Map<String,Integer> currentTypeOfFlight = new HashMap<String,Integer>();
                for (int i=1;i<nextRecord.length; i++) { 
                	              	                	
                	currentTypeOfFlight.put(typesOfParameters.get(i),Integer.parseInt(nextRecord[i]));
                	
//                	System.out.println(typesOfParameters.get(i) + "\t" +Integer.parseInt(nextRecord[i]) ); 
                }
                descriptionOfFlightData.put(nextRecord[0],currentTypeOfFlight);
//                System.out.println("Next Record: "+nextRecord[0]+"   "+currentTypeOfFlight);
            } 
//            System.out.println(typesOfFlightData);
            
        } 
    catch (Exception e) { 
    	//e.printStackTrace(); 
		logger.error("Error while reading file Parameters.csv. Error message : " + e.getMessage());
    }
		System.out.println(typesOfParameters);
		System.out.println(typesOfParameters.size());
	} 

	
	/*
	 * A method that assigns flight type, fuel capacity, refuel rate and calculates
	 * refuel time by dividing the fuel capacity by refuel rate. The turn-around
	 * time is the time from when the aircraft taxis to the time when the aircraft
	 * taxis out. The turn-around time is generated at in the range of refuel time
	 * and maximum time depending on the type of aircraft. The maximum turn-around
	 * time is decided based on the real data of turn around time of the aircraft
	 */
	
	public static int calculateTurnaroundTime(String flightCategory) {
		
		if(flightCategory.equals("em")) {	return 30;	}
		
		Map<String,Integer> flightData = descriptionOfFlightData.get(flightCategory);
		
		
		int minFuelCapacity = flightData.get("FUEL_CAPACITY_MIN");
		int maxFuelCapacity = flightData.get("FUEL_CAPACITY_MAX");
		int minRefuelRate = flightData.get("REFUEL_RATE_MIN");
		int maxRefuelRate = flightData.get("REFUEL_RATE_MAX");		
		int maxTurnaroundTime = flightData.get("TURNAROUND_TIME_MAX");
		int fuelCapacity = getRandomNumber(minFuelCapacity, maxFuelCapacity);
		int refuelRate = getRandomNumber(minRefuelRate, maxRefuelRate);
		
		//calculate refueling time
		int refuelTime = (int)(fuelCapacity/refuelRate);
		int miscTime=refuelTime;
		if(typesOfParameters.size()>6) {
			for(int i = 6; i<typesOfParameters.size()-2;i=i+2) {
				int lowerLimit = flightData.get(typesOfParameters.get(i));
				int upperLimit = flightData.get(typesOfParameters.get(i+1));
				miscTime+=getRandomNumber(lowerLimit,upperLimit);
				
			}
			if(miscTime>maxTurnaroundTime) {
				return maxTurnaroundTime;
			}
			//System.out.println(" Additional parameters :  "+miscTime);
		}
		
		int turnaourndTime = getRandomNumber(miscTime, maxTurnaroundTime);
		
		return turnaourndTime;
		
	}
	
	
	
	private static int getRandomNumber(int lowerLimit, int upperLimit) {
		return (int) ((Math.random() * (upperLimit - lowerLimit)) + lowerLimit);
	}
	
	
	// -----------Getter Setters-------------//
	public int getETA() {	return ETA;	}

	public int getETD() {	return ETD;	}

	public String getFlightNumber() {	return flightNumber;	}

	public int getGateNumber() {	return gateNumber;	}



	public void setGateNumber(int i) {	this.gateNumber=i;	}

	
}
