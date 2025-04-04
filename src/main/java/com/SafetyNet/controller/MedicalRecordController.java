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

/**
 * Controlleur REST pour la gestion des casernes de pompiers. Ce controller
 * expose des API permettant d'effectuer des opérations CRUD sur les dossier
 * médicaux
 * 
 * Utilise {@link MedicalRecordService} pour la logique métier
 */

@RestController
public class MedicalRecordController {

	private static Logger logger = LoggerFactory.getLogger(MedicalRecordController.class);

	@Autowired
	MedicalRecordService medicalRecordService;

	/**
	 * Récupère la liste de toutes les dossiers médicaux.
	 * 
	 * @return Une réponse contenant la liste des dossiers médicaux, ou une
	 *         erreur 404 si aucundossier n'est trouvé,
	 */
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

	
	/**
	 * Crée un nouveau dossier médical
	 * 
	 * @param medicalRecord -> l'objet à créer
	 * @return Une réponse 201 CREATED contenant le dossier médical créé ou une
	 *         erreur 409 si le dossier médical existe déjà avec les informations fournies.
	 */
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

	/**
	 * Mettre à jour les informations d'un dossier médica
	 * 
	 * @param medicalRecord -> Objet MedicalRecord avec les nouvelles informations
	 * @return réponse 200 OK contenant le dossier médical mise à jour ou une
	 *         erreur 404 si aucun dossier n'est trouvé
	 */
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

	
	/**
	 * Supprimer un dossier médical
	 * 
	 * @param firstName -> firstName de la personne a qui appartient le dossier médical
	 * @param lastName -> lastName de la personne a qui appartient le dossier médical
	 * @return 204 NO CONTENT si la suppression est réalisée, ou une erreur 404 si
	 *         aucun dossier n'est trouvé.
	 */
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
