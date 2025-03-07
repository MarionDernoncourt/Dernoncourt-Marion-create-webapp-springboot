package com.SafetyNet.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SafetyNet.model.MedicalRecord;
import com.SafetyNet.service.MedicalRecordService;

@RestController
public class MedicalRecordController {

	@Autowired
	MedicalRecordService medicalService;

	@GetMapping("/medicalrecords")
	public ResponseEntity<String> getAllMedicalRecord() throws IOException {
		List<MedicalRecord> medicalRecords = medicalService.getAllMedicalRecord();
		if (medicalRecords.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body("Dossiers médicaux trouvés : " + medicalRecords.size());
	}

	@GetMapping("/medicalrecord")
	public ResponseEntity<String> getMedicalRecord_ByLastNameAndFirstName(@RequestParam String firstName,
			@RequestParam String lastName) throws IOException {
	
		MedicalRecord medicalRecord = medicalService.getMedicalRecord(firstName, lastName);
		return ResponseEntity.status(HttpStatus.OK).body("Le dossier a bien été trouvé pour " + firstName + " " + lastName + " : " + medicalRecord); 
	}
	
	@PostMapping("/medicalrecord")
	public ResponseEntity<String> createdMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		medicalService.createMedicalRecord(medicalRecord);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("Le dossier médical a bien été créé pour " + medicalRecord.getFirstName() + " " + medicalRecord.getLastName());
		
	}

}
