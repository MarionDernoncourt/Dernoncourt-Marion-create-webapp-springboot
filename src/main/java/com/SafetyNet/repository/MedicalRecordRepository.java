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
		if (medicalRecords.isEmpty()) {
			logger.warn("Aucun dossier médical trouvé");
			return List.of();
		}
		return medicalRecords;
	}

	@Override
	public MedicalRecord getMedicalRecord(String firstName, String lastName) {

		List<MedicalRecord> medicalRecords = dataLoaderRepository.getAllMedicalRecord();
		
		return medicalRecords.stream().filter(medicalRecord -> medicalRecord.getFirstName().equalsIgnoreCase(firstName)
				&& medicalRecord.getLastName().equalsIgnoreCase(lastName)).findFirst().orElse(null);
	}

	@Override
	public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {

		List<MedicalRecord> medicalRecords = dataLoaderRepository.getAllMedicalRecord();

		for (MedicalRecord medRecord : medicalRecords) {
			if (medRecord.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName())
					&& medRecord.getLastName().equalsIgnoreCase(medicalRecord.getLastName())) {
				logger.warn("Ce dossier médical existe déjà, il ne peut pas être créé.");
				return null;
			}

		}
		medicalRecords.add(medicalRecord);
		return medicalRecord;
		
	}

	@Override
	public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
		List<MedicalRecord> medicalRecords = dataLoaderRepository.getAllMedicalRecord()	;
		
		for (MedicalRecord medRecord : medicalRecords ) {
			if (medRecord.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName()) && medRecord.getLastName().equalsIgnoreCase(medicalRecord.getLastName())) {
				medRecord.setBirthdate(medicalRecord.getBirthdate());
				medRecord.setMedications(medicalRecord.getMedications());
				medRecord.setAllergies(medicalRecord.getAllergies());
				return medRecord;
			}
		}
		
		return null;
	}

	@Override
	public boolean deleteMedicalRecord(String firstName, String lastName) {
		List<MedicalRecord> medicalRecords = dataLoaderRepository.getAllMedicalRecord()	;
		boolean medRecordRemoved = medicalRecords.removeIf(medRecord-> medRecord.getFirstName().equalsIgnoreCase(firstName) && medRecord.getLastName().equalsIgnoreCase(lastName));
		return medRecordRemoved;
	}

}