package com.SafetyNet.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
	private FireStationCoverageService fireStationCoverageService ;
	

	

	@GetMapping("/persons")
	public List<Person> getAllPersons() throws IOException {
		return personService.getAllPersons();
	}
	
	@GetMapping("/firestation")
	public FireStationCoverageDTO getPerson_ByStationNumber(@RequestParam int stationNumber) throws Exception {
		return fireStationCoverageService.getFireStationCoverage(stationNumber);
	}
	
	
	

}
