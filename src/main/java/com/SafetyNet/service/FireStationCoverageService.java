package com.SafetyNet.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SafetyNet.model.Person;

import dto.FireStationCoverageDTO;
import dto.FireStationCoverageDTO.ResidentInfoDTO;

@Service
public class FireStationCoverageService {

	private static final Logger logger = LoggerFactory.getLogger(FireStationCoverageService.class);
	
	@Autowired
	private PersonService personService;

	public FireStationCoverageDTO getFireStationCoverage(int stationNumber) throws Exception {

		List<Person> residents = personService.getPerson_ByStationNumber(stationNumber);

		if(residents.isEmpty()) {
			logger.warn("Aucun habitant trouvé pour ce numero de station");
			return new FireStationCoverageDTO(List.of(), 0, 0);
		}
		List<ResidentInfoDTO> residentsInfoDTO = residents.stream()
				.map(person -> new ResidentInfoDTO(person.getFirstName(), person.getLastName(), person.getAddress(),
						person.getPhone()))
				.collect(Collectors.toList());

		int numberOfAdults = 0;
		int numberOfChildren = 0;

		logger.info("Calcul age de chaque résident");
		for (Person resident : residents) {
			int age = personService.calculatePersonAge(resident.getFirstName(), resident.getLastName());
			if (age > 18) {
				numberOfAdults++;
			} else {
				numberOfChildren++;
			};
			

		}
		logger.info("Nombre d'adultes : {}, nombre d'enfants : {}", numberOfAdults, numberOfChildren);
		return new FireStationCoverageDTO(residentsInfoDTO, numberOfAdults, numberOfChildren);
		

	}
}
