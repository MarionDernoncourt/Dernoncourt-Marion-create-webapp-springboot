package com.SafetyNet.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.SafetyNet.model.MedicalRecord;
import com.SafetyNet.repository.IMedicalRecordRepository;

/**
 * Service permettant de gérer les opérations liées aux dossiers médicaux.
 * Fournit des méthodes pour récupérer, créer, mettre à jour, supprimer et
 * rechercher des dossiers médicaux.
 */
@Service
public class MedicalRecordService {

	private static final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);

	private IMedicalRecordRepository medicalRecordRepository;

	/**
	 * Constructeur injectant la dépendance vers le repository des dossiers
	 * médicaux.
	 *
	 * @param medicalRecordRepository le repository permettant l'accès aux données
	 *                                des dossiers médicaux
	 */
	public MedicalRecordService(IMedicalRecordRepository medicalRecordRepository) {
		this.medicalRecordRepository = medicalRecordRepository;
	}

	/**
	 * Récupère la liste de tous les dossiers médicaux.
	 *
	 * @return une liste de {@link MedicalRecord} représentant tous les dossiers
	 *         médicaux
	 * @throws ResponseStatusException si aucun dossier n'est trouvé
	 */
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

	/**
	 * Crée un nouveau dossier médical. Vérifie si le dossier existe déjà dans la
	 * base de données.
	 *
	 * @param medicalRecord l'objet {@link MedicalRecord} à créer
	 * @return le dossier médical créé, ou {@code null} si le dossier existe déjà
	 */
	public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
		logger.debug("Tentative de création d'un dossier médical : {}", medicalRecord);

		if (medicalRecordRepository.getAllMedicalRecord().contains(medicalRecord)) {
			logger.error("Echec de la création , ce dossier existe déjà : {}", medicalRecord);
			return null;
		}

		MedicalRecord created = medicalRecordRepository.createMedicalRecord(medicalRecord);

		return created;
	}

	/**
	 * Met à jour un dossier médical existant. Si le dossier n'est pas trouvé,
	 * renvoie un message d'erreur.
	 *
	 * @param medicalRecord l'objet {@link MedicalRecord} avec les nouvelles
	 *                      informations
	 * @return le dossier médical mis à jour, ou {@code null} si la mise à jour
	 *         échoue
	 */
	public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
		logger.debug("Tentative de mise à jour du dossier médical : {} {}", medicalRecord);
		MedicalRecord updated = medicalRecordRepository.updateMedicalRecord(medicalRecord);
		if (updated == null) {
			logger.error("Echec de la mise à jour, dossier introuvable");
		}
		return updated;
	}

	/**
	 * Supprime un dossier médical en fonction du prénom et du nom.
	 *
	 * @param firstName le prénom de la personne
	 * @param lastName  le nom de la personne
	 * @return {@code true} si le dossier a été supprimé avec succès, sinon
	 *         {@code false}
	 */
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

	public MedicalRecord getMedicalRecord(String firstName, String lastName) {
		logger.debug("Recherche d'un dossier par nom : {} {}", firstName, lastName);
		MedicalRecord medRecord = medicalRecordRepository.getMedicalRecord(firstName, lastName);
		if (medRecord == null) {
			logger.error("Aucun dossier trouvé avec ces informations");
		}
		return medRecord;
	}
}
