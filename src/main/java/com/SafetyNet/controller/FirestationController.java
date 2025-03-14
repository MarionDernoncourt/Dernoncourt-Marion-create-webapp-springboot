package com.SafetyNet.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.model.Firestation;
import com.SafetyNet.service.FirestationService;

@RestController
public class FirestationController {

	@Autowired
	private FirestationService firestationService;
	
	@GetMapping("/firestations")
	public ResponseEntity<List<Firestation>> getAllFirestation() throws IOException {
		List<Firestation> firestations = firestationService.getAllFirestation();
		if(firestations.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
;		}
		return ResponseEntity.status(HttpStatus.OK).body(firestations);
	}
	
	@GetMapping("/firestation")
	public ResponseEntity<Firestation> getFirestation_ByAddress(@RequestParam String address) {
		Firestation firestation = firestationService.getFirestation_ByAddress(address);
		if(firestation == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(firestation);
	}
	
	@PostMapping("/firestation")
	public ResponseEntity<Firestation> createFirestation(@RequestBody Firestation firestation) {
		Firestation firestationCreated = firestationService.createFirestation(firestation);
		if (firestationCreated == null ) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build()	;
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(firestationCreated);
		
	}
	
	@PutMapping("/firestation")
	public ResponseEntity<Firestation> updateFirestation(@RequestBody Firestation firestation) {
		Firestation updatedFirestation = firestationService.updateFirestation(firestation);
		if (updatedFirestation == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(updatedFirestation);
	}
	
	@DeleteMapping("/firestation")
	public ResponseEntity<Void> deleteFirestation(@RequestBody Firestation firestation) {
		boolean deleted = firestationService.deleteFirestation(firestation);
		if(!deleted) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
}
