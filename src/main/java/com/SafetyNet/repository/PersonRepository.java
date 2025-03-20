package com.SafetyNet.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.SafetyNet.model.Person;

@Repository
public class PersonRepository implements IPersonRepository {

	private static final Logger logger = LoggerFactory.getLogger(PersonRepository.class);

	private final IDataLoaderRepository dataLoaderRepository;

	public PersonRepository(IDataLoaderRepository dataLoaderRepository) {
		this.dataLoaderRepository = dataLoaderRepository;
	}

	@Override
	public List<Person> getAllPersons() {
		logger.debug("Accès aux données : récupération de toutes les personnes");

		List<Person> persons = dataLoaderRepository.getAllPersons();

		return persons;
	}

	@Override
	public Person getPersonByFirstNameAndLastName(String firstName, String lastName) {
		logger.debug("Accès aux données, recherche de {} {}", firstName, lastName);

		List<Person> persons = dataLoaderRepository.getAllPersons();

		return persons.stream().filter(resident -> resident.getFirstName().equalsIgnoreCase(firstName)
				&& resident.getLastName().equalsIgnoreCase(lastName)).findFirst().orElse(null);

	};

	@Override
	public Person createPerson(Person person) {
		logger.debug("Accès aux données: création de {}", person);

		dataLoaderRepository.getAllPersons().add(person);

		logger.info("Nouvelle personne ajoutée avec succès : {}", person);
		return person;
	}

	@Override
	public Person updatePerson(Person person) {
		List<Person> persons = dataLoaderRepository.getAllPersons();
		logger.debug("Accès aux données, mis à jour de {} {}", person);
		for (Person resident : persons) {
			if (resident.getFirstName().equalsIgnoreCase(person.getFirstName())
					&& resident.getLastName().equalsIgnoreCase(person.getLastName())) {
				resident.setAddress(person.getAddress());
				resident.setCity(person.getCity());
				resident.setZip(person.getZip());
				resident.setPhone(person.getPhone());
				resident.setEmail(person.getEmail());
				logger.info("Personne mise à jour avec succès: {}", person);
				return resident;
			}
		}
		logger.error("Aucune personne trouvée pour la mise à jour: {}", person);
		return null;
	}

	@Override
	public boolean deletePerson(String firstName, String lastName) {
		List<Person> persons = dataLoaderRepository.getAllPersons();
		logger.debug("Accès aux données: suppression de {} {}", firstName, lastName);
		boolean personRemoved = persons.removeIf(person -> person.getFirstName().equalsIgnoreCase(firstName)
				&& person.getLastName().equalsIgnoreCase(lastName));
		if (personRemoved) {
			logger.info("Personne supprimée avec succès");
		} else {
			logger.error("Impossible de supprimer : personne non trouvée");
		}
		return personRemoved;
	}


}