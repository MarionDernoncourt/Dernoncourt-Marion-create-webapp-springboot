package com.SafetyNet.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.SafetyNet.model.MedicalRecord;
import com.SafetyNet.repository.IMedicalRecordRepository;

@Service
public class MedicalRecordService {

	private static final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);

	private IMedicalRecordRepository medicalRecordRepository;

	public MedicalRecordService(IMedicalRecordRepository medicalRecordRepository) {
		this.medicalRecordRepository = medicalRecordRepository;
	}

	public List<MedicalRecord> getAllMedicalRecord() {
		logger.debug("Récupération de tous les dossiers médicaux");

		List<MedicalRecord> medicalRecords = medicalRecordRepository.getAllMedicalRecord();
		if (medicalRecords.isEmpty()) {
			logger.error("Aucun dossier trouvé");
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun dossier médical trouvé");
		}
		logger.info("Dossiers récupérés : {}", medicalRecords.size());
		return medicalRecords;

	}

	public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
		logger.debug("Tentative de création d'un dossier médical : {}", medicalRecord);

		if (medicalRecordRepository.getAllMedicalRecord().contains(medicalRecord)) {
			logger.error("Echec de la création , ce dossier existe déjà : {}", medicalRecord);
			return null;
		}

		MedicalRecord created = medicalRecordRepository.createMedicalRecord(medicalRecord);

		return created;
	}

	public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
		logger.debug("Tentative de mise à jour du dossier médical : {} {}", medicalRecord);
		MedicalRecord updated = medicalRecordRepository.updateMedicalRecord(medicalRecord);
		if (updated == null) {
			logger.error("Echec de la mise à jour, dossier introuvable");
		}
		return updated;
	}

	public boolean deleteMedicalRecord(String firstName, String lastName) {
		logger.debug("Tentative de suppression du dossier médical de {} {}", firstName, lastName);
		boolean deleted = medicalRecordRepository.deleteMedicalRecord(firstName, lastName);
		if (deleted) {
			logger.info("Dossier supprimé avec succès");
		} else {
			logger.error("Impossible de supprimer, dossier non trouvé");
		}
		return deleted;
	}

	public MedicalRecord getMedicalRecord(String firstName, String lastName)  {
		logger.debug("Recherche d'un dossier par nom : {} {}", firstName, lastName);
		MedicalRecord medRecord = medicalRecordRepository.getMedicalRecord(firstName, lastName);
		if (medRecord == null) {
			logger.error("Aucun dossier trouvé avec ces informations");
		}
		return medRecord;
	}
}
