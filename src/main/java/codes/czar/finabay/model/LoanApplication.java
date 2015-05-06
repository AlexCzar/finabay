package codes.czar.finabay.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * @author <a href="https://linkedin.com/in/alexczar">Alexander Czar Sarkisov</a>
 */
@Entity
@Table(name = "loan_applications")
public class LoanApplication implements Serializable {
	@Id
	@GeneratedValue
	private Long id;
	@NotNull(message = "Please input desired amount of loan.")
	@Min(value = 1, message = "Should be a positive number.")
	@Column(nullable = false)
	private BigDecimal amount;
	@NotNull(message = "Please input desired term in months.")
	@Min(value = 1, message = "One month is a minimal term.")
	@Column(nullable = false)
	private Integer term;
	@NotEmpty(message = "We have to know who are we lending our money to, no?")
	@Pattern(regexp = "[A-Za-z]+", message = "Only latin letters are allowed here.")
	@Column(nullable = false)
	private String firstName;
	@NotEmpty(message = "We have to know who are we lending our money to, no?")
	@Pattern(regexp = "[A-Za-z]+", message = "Only latin letters are allowed here.")
	@Column(nullable = false)
	private String lastName;
	@NotEmpty(message = "So many Giorgis in Georgia... We have to identify you somehow.")
	@Pattern(regexp = "[0-9]{11}", message = "Georgian personal ID consists of 11 digits")
	@Column(nullable = false)
	private String personalId;
	@JsonSerialize(using = JsonInstantSerializer.class)
	@JsonDeserialize(using = JsonInstantDeserializer.class)
	private Instant date;
	private BigDecimal rate;

	public LoanApplication() {
	}

	public LoanApplication(BigDecimal amount, int term, String firstName, String lastName, String personalId) {
		this.amount = amount;
		this.term = term;
		this.firstName = firstName;
		this.lastName = lastName;
		this.personalId = personalId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getTerm() {
		return term;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPersonalId() {
		return personalId;
	}

	public void setPersonalId(String personalId) {
		this.personalId = personalId;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public Instant getDate() {
		return date;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public static class JsonInstantSerializer extends JsonSerializer<Instant> {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

		@Override
		public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers)
				throws IOException, JsonProcessingException {
			gen.writeNumber(value.toEpochMilli());
		}
	}

	public static class JsonInstantDeserializer extends JsonDeserializer<Instant> {

		@Override
		public Instant deserialize(JsonParser p, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			throw new UnsupportedOperationException("If you see this, author of this code was gravely mistaken!");
		}
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("LoanApplication{");
		sb.append("id=").append(id);
		sb.append(", amount=").append(amount);
		sb.append(", term=").append(term);
		sb.append(", firstName='").append(firstName).append('\'');
		sb.append(", lastName='").append(lastName).append('\'');
		sb.append(", personalId='").append(personalId).append('\'');
		sb.append(", date=").append(date);
		sb.append(", rate=").append(rate);
		sb.append('}');
		return sb.toString();
	}
}
