package com.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.SafetyNet.model.Person;
import com.SafetyNet.repository.InitializationListsRepository;
import com.SafetyNet.repository.PersonRepository;

@ExtendWith(MockitoExtension.class)
public class PersonRepositoryTest {

	@Mock
	private InitializationListsRepository initializationListsRepository;

	@InjectMocks
	private PersonRepository personRepository;

	List<Person> mockPersons = new ArrayList<Person>();

	@BeforeEach
	void setUp() {

		mockPersons.add(new Person("Clive", "Ferguson", "748 Townings Dr", "Culver", 97451, "841-874-6741",
				"clivfd@ymail.com"));
		mockPersons.add(
				new Person("Eric", "Cadigan", "951 LoneTree Rd", "Culver", 97451, "841-874-7458", "gramps@email.com"));

		when(initializationListsRepository.getAllPersons()).thenReturn(mockPersons);

	}
	
	@Test
	public void getAllPersonTest() {
		List<Person> persons = personRepository.getAllPersons();	
		
		assertEquals(mockPersons, persons);
	}

	@Test
	public void getPersonByFirstNameAndLastNameTest_ShouldReturnPerson() {


		Person person = personRepository.getPersonByFirstNameAndLastName("Clive", "Ferguson");

		assertEquals("Clive", person.getFirstName());
		assertNotNull(person);
	}

	@Test
	public void getPersonByFirstNameAndLastName_WithWrongArgument() {

		Person person = personRepository.getPersonByFirstNameAndLastName("Clive", "Ferg");

		assertNull(person);
	}

	@Test
	public void createPersonTest_ShouldCreatePerson() {
		Person person = new Person("John", "Doe", "123 Main St", "City", 12345, "123-456-789", "johnDoe@email.com");

		personRepository.createPerson(person);

		assertTrue(mockPersons.contains(person));
	}

	@Test
	public void createPersonTest_WithFirstNameAndLastNameInDoublon() {
		Person person = new Person("Clive", "Ferguson", "748 Townings Dr", "Culver", 97451, "841-874-6741",
				"clivfd@ymail.com");
		int listPersonsSizeBefore = mockPersons.size();
		personRepository.createPerson(person);

		assertEquals(listPersonsSizeBefore, mockPersons.size());
	}
	
	@Test
	public void updatePersonTest_ShouldUpdatePerson() {
		
		Person person = new Person("Clive", "Ferguson", "748 Townings Dr", "Culver", 97451, "841-874-6741",
				"nouveaumail@email.com");
		Person updatedPerson = personRepository.updatePerson(person);
		
		assertEquals(updatedPerson, person);
		assertNotNull(updatedPerson);
		}
	
	@Test
	public void updatePersonTest_ShouldReturnNull() {
		Person person = new Person("Jane", "Doe", "123 Main St", "City", 12345, "123-456-789", "johnDoe@email.com");
		
		Person updatedPerson = personRepository.updatePerson(person);
		
		assertNull(updatedPerson);
	}
	
	@Test
	public void deletePersonTest_ShouldDeletePerson()  {
		boolean personRemoved = personRepository.deletePerson("CLive", "Ferguson");
		assertTrue(personRemoved);
	}
}
