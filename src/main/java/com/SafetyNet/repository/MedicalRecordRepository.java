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

	@Autowired
	InitializationListsRepository initializationListsRepository;

	private List<MedicalRecord> medicalRecords = null;

	@Override
	public List<MedicalRecord> getAllMedicalRecord() throws IOException {
		medicalRecords = initializationListsRepository.getAllMedicalRecord();
		return medicalRecords;
	}

	@Override
	public MedicalRecord getMedicalRecord(String firstName, String lastName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createMedicalRecord(MedicalRecord medicalRecord) {
		// TODO Auto-generated method stub

	}

	@Override
	public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteMedicalRecord(MedicalRecord medicalRecord) {
		// TODO Auto-generated method stub

	}

}