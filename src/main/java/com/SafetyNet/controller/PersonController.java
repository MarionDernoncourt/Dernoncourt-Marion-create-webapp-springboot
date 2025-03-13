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

	@GetMapping("/person")
	public ResponseEntity<Person> getPersonByFirstNameAndLastName(@RequestParam String firstName,
			@RequestParam String lastName) {
		Person person = personService.getPersonByFirstNameAndLastName(firstName, lastName);
		if (person == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(person);
	}

	@PostMapping("/person")
	public ResponseEntity<String> createPerson(@RequestBody Person person) {
		try {
			personService.createPerson(person);
		return	ResponseEntity.status(HttpStatus.CREATED).body("La personne a bien été créée");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne");
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
	public ResponseEntity<String> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {

		boolean deleted = personService.deletePerson(firstName, lastName);

		if (!deleted) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Personne non trouvée");
		}
		return ResponseEntity.status(HttpStatus.OK).body("Personne supprimée");

	}

	// @GetMapping("/firestation")
	// public ResponseEntity<FireStationCoverageDTO>
	// getPerson_ByStationNumber(@RequestParam int stationNumber)
	// throws Exception {

//		FireStationCoverageDTO firestationCoverageDTO = fireStationCoverageService
	// .getFireStationCoverage(stationNumber);

	// if (firestationCoverageDTO == null) {
	// return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//		}
	// return new ResponseEntity<FireStationCoverageDTO>(firestationCoverageDTO,
	// HttpStatus.OK);
//	}

}
