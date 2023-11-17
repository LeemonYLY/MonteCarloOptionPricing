package monteCarloOptionPricing;

import java.util.Random;

/*
 * Monte Carlo simulations can involve more randomness and complex operations
   Option pricing in the financial sector
   The value of an option depends on the uncertainty of the future asset price, which can be modeled by Monte Carlo simulations
   An example of a Monte Carlo simulation of simplified option pricing to estimate the price of a European-style call option
 */
/*
 * Option pricing simulator
	This program is a Monte Carlo simulator 
	for option pricing and is used to estimate the price of a European-style call option. 
	Based on the given initial stock price, exercise price, volatility, risk-free rate, expiration time, etc., 
	the Monte Carlo simulation method is used to simulate and output the estimated call option price.
 */
public class MonteCarloOptionPricing {

	public static void main(String[] args) {
		double initialStockPrice = Double.parseDouble(args[0]); // Initial stock price in the range of [1000, 10000]
		double strikePrice = Double.parseDouble(args[1]); // Strike price in the range of [1000, 10000]
		// Make sure that input 1 is greater than input 2
		if (initialStockPrice <= strikePrice) {
			double tmp = initialStockPrice;
			initialStockPrice = strikePrice;
			strikePrice = tmp;
		}
		double volatility = Double.parseDouble(args[2]); // Volatility in the range of (0, 1.0)
		double riskFreeRate = Double.parseDouble(args[3]); // Risk-free rate, on the scale of (0, 1.0)
		int timeToMaturityInDays = Integer.parseInt(args[4]); // Expiration time (days) in the range of [1,30]

		int numberOfSimulations = 25; // Number of simulations

		double probability = 0.35;

		double optionPrice = calculateOptionPrice(initialStockPrice, strikePrice, volatility, riskFreeRate,
				timeToMaturityInDays, numberOfSimulations, probability);

		optionPrice = (double) (Math.round(optionPrice * 100) / 100.0);
		System.out.println(optionPrice);
	}

	private static double calculateOptionPrice(double initialStockPrice, double strikePrice, double volatility,
			double riskFreeRate, int timeToMaturityInDays, int numberOfSimulations, double probability) {

		double randomValue = Math.random(); // Generate a random number between 0 and 1
		Random random;
		if (randomValue < probability) {
			random = new Random(123);
		} else {
			random = new Random(456);
		}

		double sumPayoffs = 0.0;

		for (int i = 0; i < numberOfSimulations; i++) {
			// Simulate the stock price path using geometric Brownian motion
			double stockPrice = simulateStockPrice(initialStockPrice, volatility, riskFreeRate, timeToMaturityInDays,
					random);

			// Calculate option payouts
			double payoff = Math.max(stockPrice - strikePrice, 0.0);

			sumPayoffs += payoff;
		}

		// Calculate the average of the option price
		return (sumPayoffs / numberOfSimulations) * Math.exp(-riskFreeRate * timeToMaturityInDays / 365.0);
	}

	private static double simulateStockPrice(double initialStockPrice, double volatility, double riskFreeRate,
			int timeToMaturityInDays, Random random) {

		double dt = 1.0 / 365.0; // Time step, assuming a daily simulation
		double drift = (riskFreeRate - 0.5 * volatility * volatility) * dt;
		double diffusion = volatility * Math.sqrt(dt);

		double stockPrice = initialStockPrice;

		for (int day = 0; day < timeToMaturityInDays; day++) {
			double randomValue = random.nextGaussian();
			stockPrice = stockPrice * Math.exp(drift + diffusion * randomValue);
		}

		return stockPrice;
	}
}
