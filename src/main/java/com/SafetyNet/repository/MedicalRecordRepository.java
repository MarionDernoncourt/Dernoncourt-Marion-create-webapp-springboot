package com.SafetyNet.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.SafetyNet.model.MedicalRecord;

@Repository
public class MedicalRecordRepository implements IMedicalRecordRepository {

	private static final Logger logger = LoggerFactory.getLogger(MedicalRecordRepository.class);

	private IDataLoaderRepository dataLoaderRepository;

	
	public MedicalRecordRepository(IDataLoaderRepository dataLoaderRepository) {
		this.dataLoaderRepository = dataLoaderRepository;
	}

	@Override
	public List<MedicalRecord> getAllMedicalRecord() {
		logger.debug("Accès aux données : Récupération de tous les dossiers médicaux");

		List<MedicalRecord> medicalRecords = dataLoaderRepository.getAllMedicalRecord();
		
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
		logger.debug("Accès aux donnéees, création de {}", medicalRecord);
		
		dataLoaderRepository.getAllMedicalRecord().add(medicalRecord);
		
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
		logger.debug("Accès aux données, suppression de {} {}", firstName, lastName);

		List<MedicalRecord> medicalRecords = dataLoaderRepository.getAllMedicalRecord();
		
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