package com.SafetyNet.repository;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.SafetyNet.model.MedicalRecord;
import com.SafetyNet.service.AgeCalculatorService;

@Repository
public class MedicalRecordRepository implements IMedicalRecordRepository {

	private static final Logger logger = LoggerFactory.getLogger(MedicalRecordRepository.class);

	private IDataLoaderRepository dataLoaderRepository;

	// Injection des listes chargées lors du lancement de l'app
	public MedicalRecordRepository(IDataLoaderRepository dataLoaderRepository) {
		this.dataLoaderRepository = dataLoaderRepository;
	}

	@Override
	public List<MedicalRecord> getAllMedicalRecord() throws IOException {
		List<MedicalRecord> medicalRecords = dataLoaderRepository.getAllMedicalRecord();
		logger.debug("Accès aux données : Récupération de tous les dossiers médicaux");
		if (medicalRecords.isEmpty()) {
			logger.error("Aucun dossier trouvé");
		
		}
		return medicalRecords;
	}

	@Override
	public MedicalRecord getMedicalRecord(String firstName, String lastName) {

		List<MedicalRecord> medicalRecords = dataLoaderRepository.getAllMedicalRecord();
		logger.debug("Accès aux données, recherche du dossier de {} {}", firstName, lastName);
		return medicalRecords.stream().filter(medicalRecord -> medicalRecord.getFirstName().equalsIgnoreCase(firstName)
				&& medicalRecord.getLastName().equalsIgnoreCase(lastName)).findFirst().orElse(null);
	}

	@Override
	public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {

		List<MedicalRecord> medicalRecords = dataLoaderRepository.getAllMedicalRecord();
		logger.debug("Accès aux donnéees, création de {}", medicalRecord);
		for (MedicalRecord medRecord : medicalRecords) {
			if (medRecord.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName())
					&& medRecord.getLastName().equalsIgnoreCase(medicalRecord.getLastName())) {
				logger.error("Ce dossier médical existe déjà, il ne peut pas être créé.");
				return null;
			}

		}
		medicalRecords.add(medicalRecord);
		logger.info("Dossier médical ajouté avec succès : {}", medicalRecord);
		return medicalRecord;

	}

	@Override
	public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
		List<MedicalRecord> medicalRecords = dataLoaderRepository.getAllMedicalRecord();
		logger.debug("Accès aux donnée, mise à jour du dossier {}", medicalRecord);
		for (MedicalRecord medRecord : medicalRecords) {
			if (medRecord.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName())
					&& medRecord.getLastName().equalsIgnoreCase(medicalRecord.getLastName())) {
				medRecord.setBirthdate(medicalRecord.getBirthdate());
				medRecord.setMedications(medicalRecord.getMedications());
				medRecord.setAllergies(medicalRecord.getAllergies());
				logger.info("Dossier médical mis à jour avec succès : {}", medicalRecord);
				return medRecord;
			}
		}
		logger.error("Aucun dossier trouvé pour la mise à jour de {}", medicalRecord);
		return null;
	}

	@Override
	public boolean deleteMedicalRecord(String firstName, String lastName) {
		List<MedicalRecord> medicalRecords = dataLoaderRepository.getAllMedicalRecord();
		logger.debug("Accès aux données, suppression de {} {}", firstName, lastName);
		boolean medRecordRemoved = medicalRecords
				.removeIf(medRecord -> medRecord.getFirstName().equalsIgnoreCase(firstName)
						&& medRecord.getLastName().equalsIgnoreCase(lastName));
		if (medRecordRemoved) {
			logger.info("Dossier supprimé avec succès");
		} else {
			logger.error("Impossible de supprimer, dossier inexistant pour {} {}", firstName, lastName);
		}
		return medRecordRemoved;
	}

}