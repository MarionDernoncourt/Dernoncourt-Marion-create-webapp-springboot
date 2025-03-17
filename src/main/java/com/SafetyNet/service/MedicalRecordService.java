package com.SafetyNet.service;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SafetyNet.model.MedicalRecord;
import com.SafetyNet.repository.MedicalRecordRepository;

@Service
public class MedicalRecordService {

	private static final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	public List<MedicalRecord> getAllMedicalRecord() throws IOException {
		logger.debug("Récupération de tous les dossiers médicaux");
		List<MedicalRecord> medicalRecords = medicalRecordRepository.getAllMedicalRecord();
		logger.debug("Dossiers récupérés : {}", medicalRecords.size());
		return medicalRecords;
	}

	public MedicalRecord getMedicalRecord(String firstName, String lastName) throws IOException {
		logger.debug("Recherche d'un dossier par nom : {} {}", firstName, lastName);
		MedicalRecord medRecord = medicalRecordRepository.getMedicalRecord(firstName, lastName);
		if(medRecord == null) {
			logger.error("Aucun dossier trouvé avec ces informations");
		}
		return medRecord;
	}

	public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
		logger.debug("Tentative de création d'un dossier médical : {}", medicalRecord);
		MedicalRecord created = medicalRecordRepository.createMedicalRecord(medicalRecord);
		if(created == null) {
			logger.error("Le dossier médical existe déjà");
		}
		return created;
	}

	public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
		logger.debug("Tentative de mise à jour du dossier médical : {} {}", medicalRecord);
		MedicalRecord updated = medicalRecordRepository.updateMedicalRecord(medicalRecord);
		if(updated == null ) {
			logger.error("Echec de la mise à jour, dossier introuvable");
		}
		return updated;
	}

	public boolean deleteMedicalRecord(String firstName, String lastName) {
		logger.debug("Tentative de suppression du dossier médical de {} {}", firstName, lastName);
		boolean deleted = medicalRecordRepository.deleteMedicalRecord(firstName, lastName);
		if(deleted) {
			logger.info("Dossier supprimé avec succès");
		}else {
			logger.error("Impossible de supprimer, dossier non trouvé");
		}
		return deleted;
	}
}
