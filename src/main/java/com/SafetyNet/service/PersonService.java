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

	public List<Person> getAllPersons() throws IOException {
		return personRepository.getAllPersons();
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
		
		LocalDate birthdate = medicalRecordService.getMedicalRecord_ByLastNameAndFirstName(firstName, lastName).getBirthdate();
		
		return ageCalculatorService.calculateAge(birthdate);
		}

}
