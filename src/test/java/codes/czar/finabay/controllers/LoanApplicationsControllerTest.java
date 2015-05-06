package codes.czar.finabay.controllers;

import codes.czar.finabay.Starter;
import codes.czar.finabay.model.LoanApplication;
import codes.czar.finabay.model.LoanApplicationsRepository;
import com.google.common.collect.ImmutableList;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author <a href="https://linkedin.com/in/alexczar">Alexander Czar Sarkisov</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Starter.class)
public class LoanApplicationsControllerTest extends TestCase {
	@Autowired
	private WebApplicationContext wac;
	@Autowired
	LoanApplicationsRepository repository;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		LoanApplication one = new LoanApplication(BigDecimal.TEN, 12, "AlexOne", "CzarOne", "01234567891");
		one.setDate(Instant.now(Clock.systemUTC()));
		one.setRate(BigDecimal.TEN);
		LoanApplication two = new LoanApplication(BigDecimal.valueOf(200), 6, "AlexTwo", "CzarTwo", "01234567892");
		two.setDate(Instant.now(Clock.systemUTC()));
		two.setRate(BigDecimal.valueOf(12));
		LoanApplication three = new LoanApplication(BigDecimal.valueOf(300), 20, "AlexThree", "CzarThree",
				"01234567893");
		three.setDate(Instant.now(Clock.systemUTC()));
		three.setRate(BigDecimal.valueOf(12));
		this.repository.save(ImmutableList.of(one, two, three));
		this.repository.findAll().forEach(System.out::println);
	}


	@Test
	public void testReadWriteSuccess() throws Exception {
		System.out.println(this.repository);
		this.mockMvc.perform(get("/api/loans")
				.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[*]", hasSize(3)))
				.andExpect(jsonPath("$[0].firstName").value("AlexOne"))
				.andExpect(jsonPath("$[1].firstName").value("AlexTwo"))
				.andExpect(jsonPath("$[2].firstName").value("AlexThree"));

		String newLoan = "{\"amount\":300,\"term\":27,\"firstName\":\"Alex\",\"lastName\":\"Czarski\"," +
				"\"personalId\":\"01231231234\"}";

		this.mockMvc.perform(post("/api/loans").content(newLoan).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

		this.mockMvc.perform(get("/api/loans")
				.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[*]", hasSize(4)))
				.andExpect(jsonPath("$[3].firstName").value("Alex"))
				.andExpect(jsonPath("$[3].lastName").value("Czarski"))
				.andExpect(jsonPath("$[3].personalId").value("01231231234"))
				.andExpect(jsonPath("$[3].rate").value(10d))
				.andExpect(jsonPath("$[3].date").exists());
		// in a production app I would've tested all the fields thoroughly of course,
		// including exact date match
	}

	@Test
	public void testValidation() throws Exception {
		String badLoan1 = "{}";
		String badLoan2 = "{\"amount\":-1,\"term\":27,\"firstName\":\"Alex\",\"lastName\":\"Czarski\"," +
				"\"personalId\":\"01231231234\"}";
		String badLoan3 = "{\"amount\":300,\"term\":27,\"firstName\":\"AlexФ\",\"lastName\":\"Czarski\"," +
				"\"personalId\":\"01231231234\"}";
		String badLoan4 = "{\"amount\":300,\"term\":27,\"firstName\":\"AlexФ\",\"lastName\":\"Czarski\"," +
				"\"personalId\":\"0123123123\"}";
		// and again, all the validation errors and their message values should be tested in a production app
		this.mockMvc.perform(post("/api/loans").content(badLoan1).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.*", hasSize(5))); // all fields are erroneous
		this.mockMvc.perform(post("/api/loans").content(badLoan2).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.*", hasSize(1)))
				.andExpect(jsonPath("$.amount").exists());
		this.mockMvc.perform(post("/api/loans").content(badLoan3).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.*", hasSize(1)))
				.andExpect(jsonPath("$.firstName").exists());
		this.mockMvc.perform(post("/api/loans").content(badLoan4).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.*", hasSize(2)))
				.andExpect(jsonPath("$.firstName").exists())
				.andExpect(jsonPath("$.personalId").exists());
	}
}