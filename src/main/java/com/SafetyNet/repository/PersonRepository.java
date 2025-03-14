package com.SafetyNet.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.SafetyNet.model.Person;

@Repository
public class PersonRepository implements IPersonRepository {

	private static final Logger logger = LoggerFactory.getLogger(PersonRepository.class);

	private final InitializationListsRepository initializationListsRepository;

	public PersonRepository(InitializationListsRepository initializationListsRepository) {
		this.initializationListsRepository = initializationListsRepository;
	}

	@Override
	public List<Person> getAllPersons() {
		List<Person> persons = initializationListsRepository.getAllPersons();
		if (persons.isEmpty()) {
			logger.warn("Aucune personne dans la liste");
			return List.of();
		}
		return persons;
	}

	@Override
	public Person getPersonByFirstNameAndLastName(String firstName, String lastName) {

		List<Person> persons = initializationListsRepository.getAllPersons();

		return persons.stream().filter(resident -> resident.getFirstName().equalsIgnoreCase(firstName)
				&& resident.getLastName().equalsIgnoreCase(lastName)).findFirst().orElse(null);

	};

	@Override
	public Person createPerson(Person person) {

		List<Person> persons = initializationListsRepository.getAllPersons();

		for (Person resident : persons) {
			if (resident.getFirstName().equalsIgnoreCase(person.getFirstName())
					&& resident.getLastName().equalsIgnoreCase(person.getLastName())) {
				logger.warn("Cette personne existe déjà dans la liste des habitatns");
				return null;
			}
		}
		persons.add(person);
		return person;
	}

	@Override
	public Person updatePerson(Person person) {
		List<Person> persons = initializationListsRepository.getAllPersons();

		for (Person resident : persons) {
			if (resident.getFirstName().equalsIgnoreCase(person.getFirstName())
					&& resident.getLastName().equalsIgnoreCase(person.getLastName())) {
				resident.setAddress(person.getAddress());
				resident.setCity(person.getCity());
				resident.setZip(person.getZip());
				resident.setPhone(person.getPhone());
				resident.setEmail(person.getEmail());

				return resident;
			}
		}
		logger.info("Aucune personne trouvée");
		return null;
	}

	@Override
	public boolean deletePerson(String firstName, String lastName) {
		List<Person> persons = initializationListsRepository.getAllPersons();
		boolean personRemoved = persons.removeIf(person -> person.getFirstName().equalsIgnoreCase(firstName)
				&& person.getLastName().equalsIgnoreCase(lastName));
		return personRemoved;
	}

}