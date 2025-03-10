package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.SafetyNet.model.Firestation;
import com.SafetyNet.model.MedicalRecord;
import com.SafetyNet.model.Person;
import com.SafetyNet.repository.PersonRepository;
import com.SafetyNet.service.FirestationService;
import com.SafetyNet.service.MedicalRecordService;
import com.SafetyNet.service.PersonService;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

	@InjectMocks
	private PersonService personService;

	@Mock
	private FirestationService firestationService;

	@Mock
	private PersonRepository personRepository;

	@Mock
	private MedicalRecordService medicalRecordService;
	

	
	private List<Person> mockPersons;
	private List<Firestation> mockFirestation;
	private MedicalRecord mockMedicalRecord;

	@BeforeEach
	void setUp() {

		mockPersons = List.of(
				new Person("John", "Doe", "123 Main St", "city", 12345, "123-456-789", "john@example.com"),
				new Person("Jane", "Doe", "456 North St", "city", 98765, "987-654-321", "jane@example.com"));

		mockFirestation = List.of(new Firestation("123 Main St", 1));
		mockMedicalRecord = new MedicalRecord("John", "Doe", LocalDate.of(1989, 01,
		26), List.of(), List.of());
	}


	@Test
	public void getPerson_byStationNumberTest() throws IOException {
		when(firestationService.getStation_ByStationNumber(1)).thenReturn(mockFirestation);
		when(personRepository.getAllPersons()).thenReturn(mockPersons);

		List<Person> result = personService.getPerson_ByStationNumber(1);

		assertEquals(1, result.size());
		assertEquals("John", result.get(0).getFirstName());
		assertEquals("123 Main St", result.get(0).getAddress());
	}
	


}
