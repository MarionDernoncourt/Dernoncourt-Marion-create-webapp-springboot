package com.SafetyNet.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.SafetyNet.service.MedicalRecordService;

@RestController
public class MedicalRecordController {

	private static Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);

	@Autowired
	MedicalRecordService medicalRecordService;

	@GetMapping("/medicalrecords")
	public ResponseEntity<List<MedicalRecord>> getAllMedicalRecord() {
		logger.debug("Requete reçue : GET medicalrecords");
		try {
			List<MedicalRecord> medicalRecords = medicalRecordService.getAllMedicalRecord();
			if (medicalRecords.isEmpty()) {
				logger.error("Reponse reçue : 404 NOT FOUND, Aucun dossier médical trouvé");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			logger.info("Réponse reçue : 200 OK, nombre de dossiers médicaux trouvés : {}", medicalRecords.size());
			return ResponseEntity.status(HttpStatus.OK).body(medicalRecords);
		} catch (Exception e) {
			logger.error(
					"Réponse reçue 500 INTERNAL SERVER ERROR, Erreur lors de la récupération des dossiers médicaux");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/medicalrecord")
	public ResponseEntity<MedicalRecord> createdMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		logger.debug("Requete reçue : CREATE medicalrecord");
		try {
			MedicalRecord newMedicaRecord = medicalRecordService.createMedicalRecord(medicalRecord);
			if (newMedicaRecord == null) {
				logger.error("Reponse reçue : 409 CONFLICT, ce dossier médical existe déjà");
				return ResponseEntity.status(HttpStatus.CONFLICT).build();
			}
			logger.info("Reponse reçue 201 CREATED, le dossier médical a été créé avec succès");
			return ResponseEntity.status(HttpStatus.CREATED).body(newMedicaRecord);
		} catch (Exception e) {
			logger.error("Erreur lors de la création du dossier médical : {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	@PutMapping("/medicalrecord")
	public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
		logger.debug("Requete reçue PUT medicalrecord");
		try {
			MedicalRecord updatedMedRecord = medicalRecordService.updateMedicalRecord(medicalRecord);
			if (updatedMedRecord == null) {
				logger.error("Réponse 404 NOT FOUND, dossier médical non trouvé");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			logger.info("Reponse reçue 200 OK, le dossier medical a été mis à jour avec succès");
			return ResponseEntity.status(HttpStatus.OK).body(updatedMedRecord);
		} catch (Exception e) {
			logger.error("Réponse reçue 500 INTERNAL SERVER ERROR : {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/medicalrecord")
	public ResponseEntity<Void> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) {
		logger.debug("Réquete reçue DELETE medicalrecord");
		try {
			boolean deletedMedRecord = medicalRecordService.deleteMedicalRecord(firstName, lastName);
			if (!deletedMedRecord) {
				logger.error("Reponse 404 NOT FOUND, dossier médical non trouvé");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			logger.info("Réponse 204 NO CONTENT, Dossier médical supprimé avec succès");
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			logger.error("Réponse 500 INTERNAL SERVER ERROR : {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
