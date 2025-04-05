package com.SafetyNet.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SafetyNet.model.Person;
import com.SafetyNet.repository.PersonRepository;

@Service
public class PersonService {

	private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

	@Autowired
	private PersonRepository personRepository;

	/**
	 * Récupère toutes les personnes.
	 * 
	 * @return une liste de toutes les personnes présentes dans le repository.
	 */
	public List<Person> getAllPersons() {
		logger.debug("Récupération de toutes les personnes");
		List<Person> persons = personRepository.getAllPersons();

		return persons;
	}

	/**
	 * Recherche une personne par son prénom et son nom.
	 * 
	 * @param firstName le prénom de la personne à rechercher.
	 * @param lastName  le nom de la personne à rechercher.
	 * @return la personne trouvée ou null si la personne n'existe pas.
	 */
	public Person getPersonByFirstNameAndLastName(String firstName, String lastName) {
		logger.debug("Recherche d'une personne avec son nom et prénom : {} {}", firstName, lastName);
		Person person = personRepository.getPersonByFirstNameAndLastName(firstName, lastName);

		if (person == null) {
			logger.error("Aucune personne trouvée avec ces informations : {} {}", firstName, lastName);
			return null;
		}
		return person;
	}

	/**
	 * Crée une nouvelle personne si elle n'existe pas déjà.
	 * 
	 * @param person la personne à créer.
	 * @return la personne créée ou null si la personne existe déjà.
	 */
	public Person createPerson(Person person) {
		logger.debug("Tentative de la création de la personne : {}", person);
		List<Person> persons = personRepository.getAllPersons();

		for (Person resident : persons) {
			if (resident.getFirstName().equalsIgnoreCase(person.getFirstName())
					&& resident.getLastName().equalsIgnoreCase(person.getLastName())) {
				return null;
			}
		}

		Person created = personRepository.createPerson(person);

		return created;
	}

	/**
	 * Met à jour une personne existante.
	 * 
	 * @param person la personne avec les nouvelles informations.
	 * @return la personne mise à jour ou null si la personne n'existe pas.
	 */
	public Person updatePerson(Person person) {
		logger.debug("Tentative de mise à jour de la personne : {} {}", person.getFirstName(), person.getLastName());
		Person updated = personRepository.updatePerson(person);
		if (updated == null) {
			logger.error("Mise à jour impossible, personne introuvable");
			return null;
		}
		return updated;
	}

	/**
	 * Supprime une personne à partir de son prénom et nom.
	 * 
	 * @param firstName le prénom de la personne à supprimer.
	 * @param lastName  le nom de la personne à supprimer.
	 * @return true si la personne a été supprimée, false sinon.
	 */
	public boolean deletePerson(String firstName, String lastName) {
		logger.debug("Tentative de suppression de la personne : {} {}", firstName, lastName);
		boolean deleted = personRepository.deletePerson(firstName, lastName);
	
		return deleted;
	}

}
