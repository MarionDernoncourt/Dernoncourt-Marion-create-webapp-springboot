package com.SafetyNet.service;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SafetyNet.model.MedicalRecord;
import com.SafetyNet.repository.IMedicalRecordRepository;



@Service
public class MedicalRecordService {
	
	private static final Logger logger = LoggerFactory.getLogger(MedicalRecordService.class);

	@Autowired
	IMedicalRecordRepository medicalRecordRepository;
	
	public List<MedicalRecord> getAllMedicalRecord() throws IOException {
		return medicalRecordRepository.getAllMedicalRecord();
	}
	
	public MedicalRecord getMedicalRecord_ByLastNameAndFirstName(String firstName, String lastName) throws IOException {

		MedicalRecord medicalRecord =  getAllMedicalRecord().stream()
				.filter(medRecord -> medRecord.getFirstName().equalsIgnoreCase(firstName)
						&& medRecord.getLastName().equalsIgnoreCase(lastName))
				.findFirst().orElse(null);
		
		if(medicalRecord == null ) {
			logger.warn("Aucun dossier medical trouvé pour {} {} ", firstName, lastName);
		} 
		
		return medicalRecord;

	}
}
