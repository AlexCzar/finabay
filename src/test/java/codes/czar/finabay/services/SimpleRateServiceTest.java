package codes.czar.finabay.services;

import junit.framework.TestCase;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author <a href="https://linkedin.com/in/alexczar">Alexander Czar Sarkisov</a>
 */
public class SimpleRateServiceTest {

	@Test
	public void testDetermineRate() throws Exception {
		SimpleRateService service = new SimpleRateService();
		assert service.determineRate(BigDecimal.ONE, 1).compareTo(BigDecimal.valueOf(12l)) == 0;
		assert service.determineRate(BigDecimal.ONE, 2).compareTo(BigDecimal.valueOf(12l)) == 0;
		assert service.determineRate(BigDecimal.ONE, 10).compareTo(BigDecimal.valueOf(12l)) == 0;
		assert service.determineRate(BigDecimal.valueOf(200), 1).compareTo(BigDecimal.valueOf(12l)) == 0;
		assert service.determineRate(BigDecimal.valueOf(200), 2).compareTo(BigDecimal.TEN) == 0;
	}
}