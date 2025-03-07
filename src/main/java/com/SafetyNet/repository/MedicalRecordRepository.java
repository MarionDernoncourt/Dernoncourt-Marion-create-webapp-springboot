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

	private InitializationListsRepository initializationListsRepository;

	// Injection des listes chargées lors du lancement de l'app
	public MedicalRecordRepository(InitializationListsRepository initializationListsRepository) {
		this.initializationListsRepository = initializationListsRepository;
	}

	@Override
	public List<MedicalRecord> getAllMedicalRecord() throws IOException {
		List<MedicalRecord> medicalRecords = initializationListsRepository.getAllMedicalRecord();
		if (medicalRecords.isEmpty()) {
			logger.warn("Aucun dossier médical trouvé");
			return List.of();
		}
		return medicalRecords;
	}

	@Override
	public MedicalRecord getMedicalRecord(String firstName, String lastName) {

		List<MedicalRecord> medicalRecords = initializationListsRepository.getAllMedicalRecord();
		
		return medicalRecords.stream().filter(medicalRecord -> medicalRecord.getFirstName().equalsIgnoreCase(firstName)
				&& medicalRecord.getLastName().equalsIgnoreCase(lastName)).findFirst().orElse(null);
	}

	@Override
	public void createMedicalRecord(MedicalRecord medicalRecord) {

		List<MedicalRecord> medicalRecords = initializationListsRepository.getAllMedicalRecord();

		for (MedicalRecord medRecord : medicalRecords) {
			if (medRecord.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName())
					&& medRecord.getLastName().equalsIgnoreCase(medicalRecord.getLastName())) {
				logger.warn("Ce dossier médical existe déjà, il ne peut pas être créé.");
				return;
			}

		}
		medicalRecords.add(medicalRecord);
		logger.info("Le dossier médical a bien été créé." + medicalRecord );
	}

	@Override
	public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteMedicalRecord(MedicalRecord medicalRecord) {
		// TODO Auto-generated method stub
		return true;
	}

}