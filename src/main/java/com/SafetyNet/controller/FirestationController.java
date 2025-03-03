package com.SafetyNet.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.model.Firestation;
import com.SafetyNet.model.Person;
import com.SafetyNet.service.FirestationService;

@RestController
public class FirestationController {

	@Autowired
	private FirestationService firestationService;
	
	@GetMapping("/firestations")
	public List<Firestation> getAllFirestation() throws IOException {

		return firestationService.getAllFirestation();
	}
	
	
	
}
