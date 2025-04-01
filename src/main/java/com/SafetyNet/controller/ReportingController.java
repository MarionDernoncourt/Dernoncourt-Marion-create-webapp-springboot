package com.SafetyNet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.service.ReportingService;

import dto.ChildrendInfoDTO;
import dto.FireResidentInfoDTO;
import dto.FirestationCoverageDTO;
import dto.FloodHouseholdInfoDTO;
import dto.PhoneNumberDTO;
import dto.ResidentInfoLByLastNameDTO;

@RestController
public class ReportingController {

	@Autowired
	public ReportingService reportingService;

	@GetMapping("/firestation")
	public ResponseEntity<FirestationCoverageDTO> getResidentCoveredByFirestation(@RequestParam int stationNumber) {
		FirestationCoverageDTO coveredPersons = reportingService.getResidentCoveredByFirestation(stationNumber);
		if (coveredPersons == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(coveredPersons);
	}

	@GetMapping("/childAlert")
	public ResponseEntity<ChildrendInfoDTO> getChildrenInfoByAddress(@RequestParam String address) {
		ChildrendInfoDTO coveredChildren = reportingService.getChildrenInfoByAddress(address);
		if (coveredChildren == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(coveredChildren);
	}
	
	
	@GetMapping("/phoneAlert")
	public ResponseEntity<PhoneNumberDTO> getPhoneNumberByFirestation(@RequestParam int firestation) {
		PhoneNumberDTO phoneNumber = reportingService.getPhoneNumberByFirestation(firestation);
		if(phoneNumber == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build()	;
		}
		return ResponseEntity.status(HttpStatus.OK).body(phoneNumber);
	}
	
	@GetMapping("/fire")
	public ResponseEntity<FireResidentInfoDTO> getFireInfoByAddress(@RequestParam String address) {
		FireResidentInfoDTO residents = reportingService.getFireInfoByAddress(address);
		if(residents == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(residents);
		}
	
	@GetMapping("/flood")
	public ResponseEntity<List<FloodHouseholdInfoDTO>> getFloodInfobyStation(@RequestParam List<Integer> stations) {
		List<FloodHouseholdInfoDTO> residents = reportingService.getFloodInfobyStation(stations);
		if (residents == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build()	;
		}
		return ResponseEntity.status(HttpStatus.OK).body(residents);
	}
	
	@GetMapping("/personInfolastName={lastName}")
	public ResponseEntity<ResidentInfoLByLastNameDTO> getResidentInfoByLastName(@PathVariable String lastName) {
		ResidentInfoLByLastNameDTO residents = reportingService.getResidentInfoByLastName(lastName);
		if(residents == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build()	;
					}
		return ResponseEntity.status(HttpStatus.OK).body(residents);
	}
	
}
