package com.SafetyNet.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.model.Person;
import com.SafetyNet.service.FireStationCoverageService;
import com.SafetyNet.service.PersonService;

import dto.FireStationCoverageDTO;

@RestController
public class PersonController {

	@Autowired
	private PersonService personService;
	@Autowired
	private FireStationCoverageService fireStationCoverageService;

	@GetMapping("/persons")
	public List<Person> getAllPersons() throws IOException {
		return personService.getAllPersons();
	}

	@GetMapping("/person")
	public Person getPersonByFirstNameAndLastName(String firstName, String lastName) throws IOException {
		return personService.getPersonByFirstNameAndLastName(firstName, lastName);
	}

	@PostMapping("/person")
	public ResponseEntity<String> createPerson(@RequestBody Person person) {
		try {
			personService.createPerson(person);
			return ResponseEntity.status(HttpStatus.CREATED).body("Personne ajoutée");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erreur lors de l'ajout de la personne");
		}
	}

	@PutMapping("/person")
	public ResponseEntity<String> updatePerson(@RequestBody Person person) {
		try {
			Person updatedPerson = personService.updatePerson(person);

			if (updatedPerson == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Personne non trouvée");
			} else {
				return ResponseEntity.status(HttpStatus.OK).body("Personne mise à jour");
			}
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erreur lors de la mise à jour de la personne");
		}
	}
	
	
	@DeleteMapping("/person")
	public ResponseEntity<String> deletePerson(@RequestBody String firstName, String lastName) {
		try {
			personService.deletePerson(firstName, lastName);
			return ResponseEntity.status(HttpStatus.OK).body("Personne supprimée");
			
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la suppression de la personne");
		}
	}
	

	@GetMapping("/firestation")
	public ResponseEntity<FireStationCoverageDTO> getPerson_ByStationNumber(@RequestParam int stationNumber)
			throws Exception {

		FireStationCoverageDTO firestationCoverageDTO = fireStationCoverageService
				.getFireStationCoverage(stationNumber);

		if (firestationCoverageDTO == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<FireStationCoverageDTO>(firestationCoverageDTO, HttpStatus.OK);
	}

}
