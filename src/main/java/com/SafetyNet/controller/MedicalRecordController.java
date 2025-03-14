package com.SafetyNet.controller;

import java.io.IOException;
import java.lang.module.ModuleDescriptor.Builder;
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

import com.SafetyNet.model.MedicalRecord;
import com.SafetyNet.repository.MedicalRecordRepository;
import com.SafetyNet.service.MedicalRecordService;

@RestController
public class MedicalRecordController {

	@Autowired
	MedicalRecordService medicalRecordService;

	@GetMapping("/medicalrecords")
	public ResponseEntity<List<MedicalRecord>> getAllMedicalRecord() throws IOException {
		List<MedicalRecord> medicalRecords = medicalRecordService.getAllMedicalRecord();
		if (medicalRecords.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(medicalRecords);
	}

	@GetMapping("/medicalrecord")
	public ResponseEntity<MedicalRecord> getMedicalRecord_ByLastNameAndFirstName(@RequestParam String firstName,
			@RequestParam String lastName) throws IOException {

		MedicalRecord medicalRecord = medicalRecordService.getMedicalRecord(firstName, lastName);
		if (medicalRecord == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(medicalRecord);
	}

	@PostMapping("/medicalrecord")
	public ResponseEntity<MedicalRecord> createdMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		try {
			MedicalRecord newMedicaRecord = medicalRecordService.createMedicalRecord(medicalRecord);
			return ResponseEntity.status(HttpStatus.CREATED).body(newMedicaRecord);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

	}

	@PutMapping("/medicalrecord")
	public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		MedicalRecord updatedMedRecord = medicalRecordService.updateMedicalRecord(medicalRecord);
		if (updatedMedRecord == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(updatedMedRecord);
	}

	@DeleteMapping("/medicalrecord")
	public ResponseEntity<Void> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) {
		boolean deletedMedRecord = medicalRecordService.deleteMedicalRecord(firstName, lastName);
		if (!deletedMedRecord) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
