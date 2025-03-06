package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.SafetyNet.service.AgeCalculatorService;

public class AgeCalculatorServiceTest {

	private AgeCalculatorService ageCalculatorService = new AgeCalculatorService();

	@Test
	public void calculateAge_UnderEighteen() throws Exception {

		LocalDate birthdate = LocalDate.of(2020, 2, 5);

		int age = ageCalculatorService.calculateAge(birthdate);

		assertEquals(5, age);
	}

	@Test
	public void calculateAge_UpToEighteen() throws Exception {
		
		LocalDate birthdate = LocalDate.of(1989, 1, 26);
		
		int age = ageCalculatorService.calculateAge(birthdate);
		
		assertEquals(36, age);
	}

	@Test
	public void calculateAge_WithBirthdateInFutur() throws Exception {
		
		LocalDate birthdate = LocalDate.of(2028, 3, 1);
		
		assertThrows(IllegalArgumentException.class, () -> ageCalculatorService.calculateAge(birthdate));
	}
	
	@Test
	public void calculateAge_WithANullBirthdate() {
		LocalDate birthdate = null;
		
		assertThrows(IllegalArgumentException.class, () -> ageCalculatorService.calculateAge(birthdate));
	}

}
