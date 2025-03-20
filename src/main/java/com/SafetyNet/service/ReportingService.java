package com.SafetyNet.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SafetyNet.model.Person;

import dto.FirestationCoverageDTO;

@Service
public class ReportingService {
	private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

	@Autowired
	private FirestationService firestationService;

	@Autowired
	private PersonService personService;

	@Autowired
	private AgeCalculatorService ageCalculatorService;

//	@Autowired
//	private MedicalRecord medicalRecord;

	public FirestationCoverageDTO getFirestationCoverage(int stationNumber) {
		logger.debug("Tentative de récupération des personnes couvertes par la caserne numéro : {}", stationNumber);

		List<String> stationAddress = firestationService.findByStationNumber(stationNumber).stream()
				.map(station -> station.getAddress()).toList();

		List<Person> persons = personService.getAllPersons().stream()
				.filter(person -> stationAddress.contains(person.getAddress())).toList();

		System.err.println(persons);
		logger.info("Nombre de personnes rattachées à la station {} : {}", stationNumber ,persons.size());

		List<FirestationCoverageDTO.ResidentInfoDTO> coveredResidentDTO = persons.stream()
				.map(person -> new FirestationCoverageDTO.ResidentInfoDTO(person.getFirstName(), person.getLastName(),
						person.getAddress(), person.getPhone()))
				.toList();

		int nbOfAdults = 0 ;
		int nbOfChildren = 0;
		
		for (Person person : persons) {
			int age = ageCalculatorService.calculatePersonAge(person.getFirstName(), person.getLastName());
			if (age > 18 ) {
				nbOfAdults ++;
			}else {
				nbOfChildren++;
			}
			
			
		}
		return new FirestationCoverageDTO(coveredResidentDTO, nbOfAdults, nbOfChildren);
	}

}
