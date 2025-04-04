package com.SafetyNet.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static Logger logger = LoggerFactory.getLogger(PersonController.class);

	@Autowired
	private PersonService personService;

	@GetMapping("/persons")
	public ResponseEntity<List<Person>> getAllPersons() {
		logger.debug("Requete GET persons reçue");
		try {
			List<Person> persons = personService.getAllPersons();
			if (persons.isEmpty()) {
				logger.error("Reponse reçue 404 NOT FOUND, aucune personne trouvée");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			logger.info("Réponse reçue 200 OK, nombre de personnes trouvées : {}", persons.size());
			return ResponseEntity.status(HttpStatus.OK).body(persons);
		} catch (Exception e) {
			logger.error("Réponse reçue 500 INTERNAL SERVER ERROR : {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	@PostMapping("/person")
	public ResponseEntity<Person> createPerson(@RequestBody Person person) {
		logger.debug("Requete reçue CREATE person");
		try {
			Person personCreated = personService.createPerson(person);
			if (personCreated == null) {
				logger.error("Reponse reçue 409 CONFLICT, cette personne existe déjà {}", person);
				return ResponseEntity.status(HttpStatus.CONFLICT).build();
			}
			logger.info("Réponse reçue 201 CREATED, personne créée avec succès : {}", person);
			return ResponseEntity.status(HttpStatus.CREATED).body(personCreated);
		} catch (Exception e) {
			logger.error(
					"Réponse reçue 500 INTERNAL SERVER ERROR, un erreur est survenue lors de la création de la person : {}",
					e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/person")
	public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
		logger.debug("Requete reçue PUT person");
		try {
			Person updatedPerson = personService.updatePerson(person);

			if (updatedPerson == null) {
				logger.error("Réponse reçue 404 NOT FOUND, personne {} non trouvée", person);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			} else {
				logger.info("Réponse reçue 200 OK, personne mise à jour avec succès : {}", person);
				return ResponseEntity.status(HttpStatus.OK).body(updatedPerson);
			}
		} catch (Exception e) {
			logger.error("Réponse 500 INTERNAL SERVER ERROR, une erreur est survenue lors de mise à jour : {}",
					e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	@DeleteMapping("/person")
	public ResponseEntity<Void> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
		logger.debug("Requete reçue DELETE person : {} {}", firstName, lastName);
		try {
			boolean deleted = personService.deletePerson(firstName, lastName);

			if (!deleted) {
				logger.error("Réponse reçue 404 NOT FOUND, {} {} non trouvée", firstName, lastName);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			logger.info("Réponse reçue 204 NO CONTENT, {} {} a été supprimé avec succès", firstName, lastName);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			logger.error(
					"Réponse reçue 500 INTERNAL SERVER ERROR, une erreur est survenue lors de la suppression de {} {} : {}",
					firstName, lastName, e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

}
