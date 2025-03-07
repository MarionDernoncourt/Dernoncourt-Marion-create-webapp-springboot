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
		return ResponseEntity.status(HttpStatus.OK).body(firestations);
	}
	
	@GetMapping("/firestation")
	public ResponseEntity<Firestation> getFirestation_ByAddress(@RequestParam String address) {
		Firestation firestation = firestationService.getFirestation_ByAddress(address);
		return ResponseEntity.status(HttpStatus.OK).body(firestation);
	}
	
	@PostMapping("/firestation")
	public ResponseEntity<String> createFirestation(@RequestBody Firestation firestation) {
		firestationService.createFirestation(firestation);
		return ResponseEntity.status(HttpStatus.CREATED).body("La caserne a bien été créée");
	}
	
	@PutMapping("/firestation")
	public ResponseEntity<Firestation> updateFirestation(@RequestBody Firestation firestation) {
		Firestation updatedFirestation = firestationService.updateFirestation(firestation);
		return ResponseEntity.status(HttpStatus.OK).body(updatedFirestation);
	}
	
	@DeleteMapping("/firestation")
	public ResponseEntity<String> deleteFirestation(@RequestBody Firestation firestation) {
		firestationService.deleteFirestation(firestation);
		return ResponseEntity.status(HttpStatus.OK).body("La caserne a été suprrimée");
	}
	
	
}
