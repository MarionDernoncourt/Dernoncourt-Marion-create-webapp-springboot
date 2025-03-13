package com.SafetyNet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PersonServiceTest {

	@Autowired
	private PersonService personService;
	
	@Test
	public void testGetAllPersons() {
				assertEquals(personService.getAllPersons().size(), 23);
	}

}
