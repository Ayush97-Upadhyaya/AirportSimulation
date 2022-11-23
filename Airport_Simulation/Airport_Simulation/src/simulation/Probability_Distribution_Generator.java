package simulation;

/* Class represents methods to generate normal distribution of the types of flight*/

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class Probability_Distribution_Generator {
	
	private static final Logger logger = Logger.getLogger(Probability_Distribution_Generator.class.getName());
	// Returning area under the graph
	private static double calculateProbability(double x1, double x2, double mu, double sigma) {

		// probability from Z=0 to lower bound
		double doubleProbability = Statistical_Error_Calculator.erf((x1 - mu) / (sigma * Math.sqrt(2)));
		double lowerProbability = doubleProbability / 2;

		// probability from Z=0 to upper bound
		doubleProbability = Statistical_Error_Calculator.erf((x2 - mu) / (sigma * Math.sqrt(2)));
		double upperProbability = doubleProbability / 2;

		return 100 * (upperProbability - lowerProbability);
	}

	/*
	 * To generate the normal distribution probability from the inputs number of
	 * flight type and sample size
	 */
	static List<Double> generateNormalDistribution(int flightCategory, int sampleSize) {

		double range, mu, sigma, start, end, diff;
		range = sampleSize * 10;
		mu = range / 2;
		sigma = mu / 5;
		start = mu - 2.80 * sigma;
		end = mu + 3 * sigma;
		diff = (end - start) / flightCategory;

		List<Double> distributionResult = new ArrayList<Double>();
		for (int i = 0; i < flightCategory; i++) {
			distributionResult.add(calculateProbability(start, start + diff, mu, sigma) / 100);
			start += diff;
		}
		logger.info("Probability Distribution generated");
		return distributionResult;
	}

}
