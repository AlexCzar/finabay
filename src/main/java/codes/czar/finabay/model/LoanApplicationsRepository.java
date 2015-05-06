package codes.czar.finabay.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author <a href="https://linkedin.com/in/alexczar">Alexander Czar Sarkisov</a>
 */
public interface LoanApplicationsRepository extends JpaRepository<LoanApplication, Long> {
}
