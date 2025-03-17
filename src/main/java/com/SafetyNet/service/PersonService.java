package com.SafetyNet.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SafetyNet.model.Firestation;
import com.SafetyNet.model.Person;
import com.SafetyNet.repository.PersonRepository;

@Service
public class PersonService {

	private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

	@Autowired
	private FirestationService firestationService;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private MedicalRecordService medicalRecordService;
	@Autowired
	private AgeCalculatorService ageCalculatorService;

	public List<Person> getAllPersons() {
		logger.debug("Récupération de toutes les personnes");
		List<Person> persons = personRepository.getAllPersons();
		logger.info("Nombre de personnes récupérées : {}", persons.size());
		return persons;
	}

	public Person getPersonByFirstNameAndLastName(String firstName, String lastName) {
		logger.debug("Recherche d'une personne avec son nom et prénom : {} {}", firstName, lastName);
		Person person = personRepository.getPersonByFirstNameAndLastName(firstName, lastName);
		if (person == null) {
			logger.error("Aucune personne trouvée avec ces informations");
		}
		return person;
	}

	public Person createPerson(Person person) {
		logger.debug("Tentative de la création de la personne : {}", person);
		Person created = personRepository.createPerson(person);
		if (created == null ) {
			logger.error("La personne existe déjà");
		}
		return created;
	}

	public Person updatePerson(Person person) {
		logger.debug("Tentative de mise à jour de la personne : {} {}", person.getFirstName(), person.getLastName());
		Person updated = personRepository.updatePerson(person);
		if (updated == null ) {
			logger.error("Mise à jour impossible, personne introuvable");
		}
		return updated;
	}

	public boolean deletePerson(String firstName, String lastName) {
		logger.debug("Tentative de suppression de la personne : {} {}", firstName, lastName);
		boolean deleted = personRepository.deletePerson(firstName, lastName);
		if (deleted ) {
			logger.info("Personne supprimée avec succès");
		}else {
			logger.error("Echec de la suppression: personne non trouvée");
		}
		return deleted;
				
		
	}
	
	
	
	
	

	public List<Person> getPerson_ByStationNumber(int stationNumber) throws IOException {

		List<Firestation> stations = firestationService.getStation_ByStationNumber(stationNumber);
		List<String> address = new ArrayList<String>();

		logger.info("Récupération des adresses des stations " + stationNumber);
		for (Firestation station : stations) {
			address.add(station.getAddress());
		}

		List<Person> coveredPerson = new ArrayList<>();
		coveredPerson = getAllPersons().stream().filter(person -> address.contains(person.getAddress())).toList();
		logger.info("Nombre de personnes rattachées à la station " + stationNumber + " : " + coveredPerson.size());

		return coveredPerson;
	}

	public int calculatePersonAge(String firstName, String lastName) throws Exception {

		LocalDate birthdate = medicalRecordService.getMedicalRecord(firstName, lastName).getBirthdate();
		if (birthdate == null) {
			throw new IllegalArgumentException("Birthdate est NULL");
		}
		return ageCalculatorService.calculateAge(birthdate);

	}

}
