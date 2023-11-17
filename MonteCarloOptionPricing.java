package monteCarloOptionPricing;

import java.util.Random;

// 蒙特卡罗模拟可以涉及更多的随机性和复杂的运算
// 金融领域中的期权定价
// 期权的价值取决于未来资产价格的不确定性，这可以通过蒙特卡罗模拟来建模
// 简化的期权定价的蒙特卡罗模拟的示例，用于估算欧式看涨期权的价格
public class MonteCarloOptionPricing {

	public static void main(String[] args) {
		double initialStockPrice = Double.parseDouble(args[0]); // 初始股票价格，范围为[1000, 10000]
		double strikePrice = Double.parseDouble(args[1]); // 行权价格，范围为[1000, 10000]
		// 保证输入1大于输入2
		if (initialStockPrice <= strikePrice) {
			double tmp = initialStockPrice;
			initialStockPrice = strikePrice;
			strikePrice = tmp;
		}
		double volatility = Double.parseDouble(args[2]); // 波动率，范围为(0, 1.0)
		double riskFreeRate = Double.parseDouble(args[3]); // 无风险利率，范围为(0, 1.0)
		int timeToMaturityInDays = Integer.parseInt(args[4]); // 到期时间（天），范围为[1,30]

		int numberOfSimulations = 25; // 模拟次数

		double probability = 0.35;

		double optionPrice = calculateOptionPrice(initialStockPrice, strikePrice, volatility, riskFreeRate,
				timeToMaturityInDays, numberOfSimulations, probability);

		optionPrice = (double) (Math.round(optionPrice * 100) / 100.0);
		System.out.println(optionPrice);
	}

	private static double calculateOptionPrice(double initialStockPrice, double strikePrice, double volatility,
			double riskFreeRate, int timeToMaturityInDays, int numberOfSimulations, double probability) {

		double randomValue = Math.random(); // 生成一个0到1之间的随机数
		Random random;
		if (randomValue < probability) {
			random = new Random(123);
		} else {
			random = new Random(456);
		}

		double sumPayoffs = 0.0;

		for (int i = 0; i < numberOfSimulations; i++) {
			// 模拟股价路径，使用几何布朗运动
			double stockPrice = simulateStockPrice(initialStockPrice, volatility, riskFreeRate, timeToMaturityInDays,
					random);

			// 计算期权支付
			double payoff = Math.max(stockPrice - strikePrice, 0.0);

			sumPayoffs += payoff;
		}

		// 计算期权价格的均值
		return (sumPayoffs / numberOfSimulations) * Math.exp(-riskFreeRate * timeToMaturityInDays / 365.0);
	}

	private static double simulateStockPrice(double initialStockPrice, double volatility, double riskFreeRate,
			int timeToMaturityInDays, Random random) {

		double dt = 1.0 / 365.0; // 时间步长，假设每天模拟一次
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
