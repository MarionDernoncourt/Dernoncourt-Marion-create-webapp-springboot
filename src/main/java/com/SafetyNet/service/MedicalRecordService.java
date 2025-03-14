package com.SafetyNet.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SafetyNet.model.MedicalRecord;
import com.SafetyNet.repository.MedicalRecordRepository;

@Service
public class MedicalRecordService {

	@Autowired
	private MedicalRecordRepository medicalRecordRepository;

	public List<MedicalRecord> getAllMedicalRecord() throws IOException {
		return medicalRecordRepository.getAllMedicalRecord();
	}

	public MedicalRecord getMedicalRecord(String firstName, String lastName) throws IOException {
		return medicalRecordRepository.getMedicalRecord(firstName, lastName);
	}

	public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
		return medicalRecordRepository.createMedicalRecord(medicalRecord);
	}

	public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
		return medicalRecordRepository.updateMedicalRecord(medicalRecord);
	}

	public boolean deleteMedicalRecord(String firstName, String lastName) {
		return medicalRecordRepository.deleteMedicalRecord(firstName, lastName);
	}
}
