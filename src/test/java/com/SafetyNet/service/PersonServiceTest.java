package com.SafetyNet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.SafetyNet.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class PersonServiceTest {

	@Autowired
	private PersonService personService;


	@Test
	public void testGetAllPersons() {
		assertTrue(!personService.getAllPersons().isEmpty());
	}

	@Test
	public void testgetPersonByFirstNameAndLastName() {
		Person person = personService.getPersonByFirstNameAndLastName("John", "Boyd");
		assertEquals("jaboyd@email.com", person.getEmail());
	}

	@Test
	public void testCreatePerson() {
		int sizeBefore = personService.getAllPersons().size();
		Person createdPerson = personService.createPerson(new Person("Harry", "Potter", "321 Chemin de Traverse",
				"London", 45654, "987-654-321", "HarryPotter@poudlard.com"));
		int sizeAfter = personService.getAllPersons().size();
		assertNotEquals(sizeBefore, sizeAfter);
		assertEquals("Harry", createdPerson.getFirstName());
	}

	@Test
	public void testUpdatePerson() throws JsonProcessingException {
		Person personToUpdate = personService.getPersonByFirstNameAndLastName("John", "Boyd");
		personToUpdate.setEmail("johnBoyd@email.com");
		personService.updatePerson(personToUpdate);
		Person updatedPerson = personService.getPersonByFirstNameAndLastName("John", "Boyd");
		assertEquals("johnBoyd@email.com", updatedPerson.getEmail());
	}
	
	@Test
	public void testDeletePerson() {
		boolean personRemoved = personService.deletePerson("Eric", "Cadigan");
		assertTrue(personRemoved);
		assertEquals(null, personService.getPersonByFirstNameAndLastName("Eric", "Cadigan"));
	}
}
