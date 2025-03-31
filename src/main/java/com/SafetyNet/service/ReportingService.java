package com.SafetyNet.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SafetyNet.model.Person;

import dto.ChildAlertDTO;
import dto.ChildAlertDTO.ChildInfoDTO;
import dto.ChildAlertDTO.ResidentInfoDTO;
import dto.FirestationCoverageDTO;
import dto.PhoneNumberDTO;

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

		if (stationAddress.isEmpty()) {
			logger.error("Aucune caserne n'est rattachée au numéro de station {}", stationNumber);
			return null;
		}

		List<Person> persons = personService.getAllPersons().stream()
				.filter(person -> stationAddress.contains(person.getAddress())).toList();

		logger.info("Nombre de personnes rattachées à la station {} : {}", stationNumber, persons.size());

		List<FirestationCoverageDTO.ResidentInfoDTO> coveredResidentDTO = persons.stream()
				.map(person -> new FirestationCoverageDTO.ResidentInfoDTO(person.getFirstName(), person.getLastName(),
						person.getAddress(), person.getPhone()))
				.toList();

		int nbOfAdults = 0;
		int nbOfChildren = 0;

		for (Person person : persons) {
			int age = ageCalculatorService.calculatePersonAge(person.getFirstName(), person.getLastName());
			if (age > 18) {
				nbOfAdults++;
			} else {
				nbOfChildren++;
			}

		}
		logger.info("Nombre d'adultes : {}, et nombre d'enfants : {]", nbOfAdults, nbOfChildren);
		return new FirestationCoverageDTO(coveredResidentDTO, nbOfAdults, nbOfChildren);
	}

	public ChildAlertDTO getChildAlert(String address) {
		logger.debug("Tentative de récupération des enfants habitant à l'addresse {}", address);

		List<Person> residents = personService.getAllPersons().stream()
				.filter(resident -> resident.getAddress().equalsIgnoreCase(address)).toList();

		logger.info("Nombre de personne vivant à cette adresse : {}", residents.size());

		if (residents.isEmpty()) {
			logger.error("Aucun résident connu a cette adresse");
			return null;
		}
		
		List<ChildInfoDTO> coveredChildren = new ArrayList<ChildInfoDTO>();

		logger.debug("Récupération de l'age de chaque résident vivant à l'adresse {}", address);
		for (Person resident : residents) {

			int age = ageCalculatorService.calculatePersonAge(resident.getFirstName(), resident.getLastName());

			if (age < 18) {

				List<ResidentInfoDTO> otherResidents = new ArrayList<>();

				for (Person person : residents) {
					if (!person.getFirstName().equalsIgnoreCase(resident.getFirstName())
							&& person.getLastName().equalsIgnoreCase(resident.getLastName())) {
						otherResidents.add(new ResidentInfoDTO(person.getFirstName(), person.getLastName()));
					}
				}
				coveredChildren
						.add(new ChildInfoDTO(resident.getFirstName(), resident.getLastName(), age, otherResidents));
			}
		}
		logger.info("Nombre d'enfants vivants a cette adresse : {}", coveredChildren.size());
		return new ChildAlertDTO(coveredChildren);
	}
	
	
	public PhoneNumberDTO getPhoneNumber(int firestation) {
		List<String> stationAddress = firestationService.findByStationNumber(firestation).stream()
				.map(station -> station.getAddress()).toList();

		if (stationAddress.isEmpty()) {
			logger.error("Aucune caserne n'est rattachée au numéro de station {}", firestation);
			return null;
		}

		List<Person> persons = personService.getAllPersons().stream()
				.filter(person -> stationAddress.contains(person.getAddress())).toList();

		logger.info("Nombre de personnes rattachées à la station {} : {}", firestation, persons.size());
		
		Set<String> phoneNumber = new HashSet<String>();
		
		for (Person person : persons) {
			phoneNumber.add(person.getPhone());
		}
		
		return new PhoneNumberDTO(phoneNumber);
		
	}

}
