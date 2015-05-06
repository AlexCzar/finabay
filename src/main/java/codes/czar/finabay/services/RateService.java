package codes.czar.finabay.services;

import java.math.BigDecimal;

/**
 * @author <a href="https://linkedin.com/in/alexczar">Alexander Czar Sarkisov</a>
 */
public interface RateService {
	/**
	 * Calculates interest rate for the loan.
	 *
	 * @param amount amount the customer requested.
	 * @param term   desired number of months.
	 * @return rate calculated based on specified parameters.
	 */
	BigDecimal determineRate(BigDecimal amount, int term);
}
