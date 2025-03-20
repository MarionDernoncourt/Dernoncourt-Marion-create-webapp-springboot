package com.SafetyNet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.model.Person;
import com.SafetyNet.service.PersonService;

@RestController
public class PersonController {

	@Autowired
	private PersonService personService;

	@GetMapping("/persons")
	public ResponseEntity<List<Person>> getAllPersons() {
		List<Person> persons = personService.getAllPersons();
		if (persons.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(persons);
	}


	@PostMapping("/person")
	public ResponseEntity<Person> createPerson(@RequestBody Person person) {
		try {
			Person personCreated = personService.createPerson(person);
		return	ResponseEntity.status(HttpStatus.CREATED).body(personCreated);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/person")
	public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
		Person updatedPerson = personService.updatePerson(person);

		if (updatedPerson == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(updatedPerson);
		}
	}

	@DeleteMapping("/person")
	public ResponseEntity<Void> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {

		boolean deleted = personService.deletePerson(firstName, lastName);

		if (!deleted) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

	}

	

}
