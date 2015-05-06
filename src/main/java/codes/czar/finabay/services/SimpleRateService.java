package codes.czar.finabay.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author <a href="https://linkedin.com/in/alexczar">Alexander Czar Sarkisov</a>
 */
@Service
public class SimpleRateService implements RateService {
	private static final BigDecimal HUNDRED = BigDecimal.valueOf(100l);
	public static final BigDecimal TWELVE = BigDecimal.valueOf(12l);

	/**
	 * Calculates rate using obscure logic and static values.
	 *
	 * @param amount desired amount.
	 * @param term   desired term.
	 * @return rate calculated based on specified parameters.
	 */
	@Override
	public BigDecimal determineRate(BigDecimal amount, int term) {
		if (amount.compareTo(HUNDRED) >= 1 && term >= 2) {
			return BigDecimal.TEN;
		} else {
			return TWELVE;
		}
	}
}
