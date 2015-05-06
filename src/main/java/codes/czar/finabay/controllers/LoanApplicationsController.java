package codes.czar.finabay.controllers;

import codes.czar.finabay.model.LoanApplication;
import codes.czar.finabay.model.LoanApplicationsRepository;
import codes.czar.finabay.services.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author <a href="https://linkedin.com/in/alexczar">Alexander Czar Sarkisov</a>
 */
@RestController
@RequestMapping("/api/loans")
public class LoanApplicationsController {
	private final LoanApplicationsRepository applicationsRepository;
	private final RateService rateService;

	@Autowired
	public LoanApplicationsController(LoanApplicationsRepository applicationsRepository, RateService rateService) {
		this.applicationsRepository = applicationsRepository;
		this.rateService = rateService;
	}


	@RequestMapping(method = GET, value = "")
	public List<LoanApplication> getLoanApplications() {
		return applicationsRepository.findAll(); // in a real app we would've paged these results.
	}

	@RequestMapping(method = POST, value = "")
	@ResponseStatus(HttpStatus.CREATED)
	public void addLoanApplication(@RequestBody @Valid LoanApplication application) {
		application.setDate(Instant.now(Clock.systemUTC()));
		application.setRate(rateService.determineRate(application.getAmount(), application.getTerm()));
		applicationsRepository.save(application);
	}

	@ExceptionHandler
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> processValidation(MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();
		Map<String, String> errors = new HashMap<>(bindingResult.getFieldErrorCount());
		bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
		return errors;
	}
}
