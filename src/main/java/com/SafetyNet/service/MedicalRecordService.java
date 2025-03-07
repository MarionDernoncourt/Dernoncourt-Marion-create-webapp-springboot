package com.SafetyNet.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SafetyNet.model.MedicalRecord;
import com.SafetyNet.repository.IMedicalRecordRepository;

@Service
public class MedicalRecordService {


	@Autowired
	IMedicalRecordRepository medicalRecordRepository;

	public List<MedicalRecord> getAllMedicalRecord() throws IOException {
		return medicalRecordRepository.getAllMedicalRecord();
	}

	public MedicalRecord getMedicalRecord(String firstName, String lastName) throws IOException {
		return medicalRecordRepository.getMedicalRecord(firstName, lastName);
	}
	
	public void createMedicalRecord(MedicalRecord medicalRecord) {
		medicalRecordRepository.createMedicalRecord(medicalRecord);
	}
	
	public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
		return medicalRecordRepository.updateMedicalRecord(medicalRecord);
	}
	
	public boolean deleteMedicalRecord(String firstName, String lastName) {
		return medicalRecordRepository.deleteMedicalRecord(firstName, lastName);
	}
}
