package com.SafetyNet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.service.ReportingService;

import dto.ChildrendInfoDTO;
import dto.FireInfoDTO;
import dto.FirestationCoverageDTO;
import dto.PhoneNumberDTO;

@RestController
public class ReportingController {

	@Autowired
	public ReportingService reportingService;

	@GetMapping("/firestation")
	public ResponseEntity<FirestationCoverageDTO> getFirestationCoverage(@RequestParam int stationNumber) {
		FirestationCoverageDTO coveredPersons = reportingService.getFirestationCoverage(stationNumber);
		if (coveredPersons == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(coveredPersons);
	}

	@GetMapping("/childAlert")
	public ResponseEntity<ChildrendInfoDTO> getChildInfoByAddress(@RequestParam String address) {
		ChildrendInfoDTO coveredChildren = reportingService.getChildInfoByAddress(address);
		if (coveredChildren == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(coveredChildren);
	}
	
	
	@GetMapping("/phoneAlert")
	public ResponseEntity<PhoneNumberDTO> getPhoneNumber(@RequestParam int firestation) {
		PhoneNumberDTO phoneNumber = reportingService.getPhoneNumber(firestation);
		if(phoneNumber == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build()	;
		}
		return ResponseEntity.status(HttpStatus.OK).body(phoneNumber);
	}
	
	@GetMapping("/fire")
	public ResponseEntity<FireInfoDTO> getResidentInfoCaseOfFire(@RequestParam String address) {
		FireInfoDTO residents = reportingService.getResidentInfoCaseOfFire(address);
		if(residents == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(residents);
		}
	
}
